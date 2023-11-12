package com.ho8278.data.repository

import androidx.collection.LruCache
import com.ho8278.data.BookRepository
import com.ho8278.data.remote.BookService
import com.ho8278.data.repository.model.BooksResult
import com.ho8278.data.repository.model.SearchResult
import com.ho8278.data.repository.model.toBook
import com.ho8278.data.repository.model.toBookResult
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.max

class BookRepositoryImpl(private val bookService: BookService) : BookRepository {

    private val bookCache = LruCache<String, BooksResult>(30)

    /**
     * [query] 는 or(|) 와 not(-) 연산을 지원한다. 검색어는 최대 2개를 사용할 수 있다.
     *
     * @param query 검색어.
     * @param page 검색할 페이지.
     */
    override suspend fun searchBook(query: String, page: Int): SearchResult {
        val isOrOperation = query.contains("|")
        val isNotOperation = query.contains("-")

        require(!isOrOperation || !isNotOperation) { "Query string format is wrong." }

        return if (isOrOperation) {
            getOrQuery(query, page)
        } else if (isNotOperation) {
            getNotQuery(query, page)
        } else {
            getQuery(query, page)
        }
    }

    private suspend fun getOrQuery(query: String, page: Int): SearchResult = coroutineScope {
        val queries = query.split("|")
            .filter { it.isNotEmpty() }
            .map { it.trim() }

        require(queries.size < 3) { "Query string format is wrong." }

        if (queries.size == 2) {
            val queryFirst = async { bookService.searchBook(queries[0], page) }
            val querySecond = async { bookService.searchBook(queries[1], page) }

            val queryFirstResult = queryFirst.await()
            val querySecondResult = querySecond.await()

            val booksList = (queryFirstResult.books + querySecondResult.books).sortedBy { it.title }
                .map { it.toBook() }

            SearchResult(
                max(queryFirstResult.total, querySecondResult.total),
                page,
                booksList
            )
        } else if (queries.size == 1) {
            getQuery(queries[0], page)
        } else {
            SearchResult(0, page, emptyList())
        }
    }

    private suspend fun getNotQuery(query: String, page: Int): SearchResult {
        val queries = query.split("-")
            .filter { it.isNotEmpty() }
            .map { it.trim() }

        require(queries.size < 3) { "Query string format is wrong." }

        return if (queries.size == 2) {
            val queryResult = bookService.searchBook(queries[0], page)

            val booksList = queryResult.books.filter { !it.title.contains(queries[1]) }
                .sortedBy { it.title }
                .map { it.toBook() }

            SearchResult(
                queryResult.total,
                page,
                booksList
            )
        } else if (queries.size == 1) {
            getQuery(queries[0], page)
        } else {
            SearchResult(0, page, emptyList())
        }
    }

    private suspend fun getQuery(query: String, page: Int): SearchResult {
        val queryResult = bookService.searchBook(query, page)
        val booksList = queryResult.books.sortedBy { it.title }
            .map { it.toBook() }

        return SearchResult(
            queryResult.total,
            page,
            booksList
        )
    }

    override suspend fun getBookDetail(isbn: String): BooksResult {
        val cachedValue = bookCache.get(isbn)

        return if (cachedValue == null) {
            val bookDetail = bookService.getBookDetail(isbn).toBookResult()
            bookCache.put(isbn, bookDetail)
            bookDetail
        } else {
            cachedValue
        }
    }
}