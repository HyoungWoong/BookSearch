package com.ho8278.data.remote

import com.ho8278.data.remote.model.RemoteBooksResult
import com.ho8278.data.remote.model.RemoteSearchResult
import retrofit2.http.GET
import retrofit2.http.Path

interface BookService {
    @GET("search/{query}/{page}")
    suspend fun searchBook(@Path("query") query: String, @Path("page") path: Int = 1): RemoteSearchResult

    @GET("books/{isbn}")
    suspend fun getBookDetail(@Path("isbn") isbn: String): RemoteBooksResult
}