package com.ho8278.data.repository.model

data class SearchResult(
    val total: Int,
    val page: Int,
    val books: List<Book>
)