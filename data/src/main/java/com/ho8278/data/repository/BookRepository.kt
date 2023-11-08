package com.ho8278.data.repository

import com.ho8278.data.remote.BookService
import com.ho8278.data.repository.model.Book
import com.ho8278.data.repository.model.BooksResult
import com.ho8278.data.repository.model.SearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.max

class BookRepository(private val bookService: BookService) {
    /**
     * [query] 는 or 와 not 연산을 지원한다. 검색어는 최대 2개를 사용할 수 있다.
     *
     */
    suspend fun searchBook(query: String, page: Int = 1): SearchResult {
        if (query.contains("|")) {
        }
        return getOrQuery(query, page)
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
                .map { Book(it.title, it.subtitle, it.isbn13, it.price, it.image, it.url) }

            SearchResult(
                max(queryFirstResult.total, querySecondResult.total),
                page,
                booksList
            )
        } else if (queries.size == 1) {
            val queryResult = bookService.searchBook(queries[0], page)

            SearchResult(
                queryResult.total,
                page,
                queryResult.books.map {
                    Book(
                        it.title,
                        it.subtitle,
                        it.isbn13,
                        it.price,
                        it.image,
                        it.url
                    )
                }
            )
        } else {
            SearchResult(0, page, emptyList())
        }
    }

    suspend fun getBookDetail(isbn: String): BooksResult {
        return BooksResult("", "", "", "", "", "", "", "", "", "", "", "", "", "")
    }
}