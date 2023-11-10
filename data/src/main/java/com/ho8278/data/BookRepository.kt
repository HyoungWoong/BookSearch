package com.ho8278.data

import com.ho8278.data.repository.model.BooksResult
import com.ho8278.data.repository.model.SearchResult

interface BookRepository {
    suspend fun searchBook(query: String, page: Int = 1): SearchResult
    suspend fun getBookDetail(isbn: String): BooksResult
}