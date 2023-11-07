package com.ho8278.data.remote

import com.ho8278.data.remote.model.BooksResult
import com.ho8278.data.remote.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Path

interface BookService {
    @GET("search/{query}/{page}")
    suspend fun searchBook(@Path("query") query: String, @Path("page") path: Int = 0): SearchResult

    @GET("books/{isbn}")
    suspend fun getBookDetail(@Path("isbn") isbn: String): BooksResult
}