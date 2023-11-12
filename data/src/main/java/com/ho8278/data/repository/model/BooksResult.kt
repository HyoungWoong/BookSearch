package com.ho8278.data.repository.model

import com.ho8278.data.remote.model.RemoteBooksResult
import kotlinx.serialization.Serializable

@Serializable
data class BooksResult(
    val title: String,
    val subtitle: String,
    val authors: String,
    val publisher: String,
    val language: String,
    val isbn10: String,
    val isbn13: String,
    val pages: String,
    val year: String,
    val rating: String,
    val desc: String,
    val price: String,
    val image: String,
    val url: String,
    val pdf: Map<String, String> = emptyMap()
)

internal fun RemoteBooksResult.toBookResult(): BooksResult {
    return BooksResult(
        title,
        subtitle,
        authors,
        publisher,
        language,
        isbn10,
        isbn13,
        pages,
        year,
        rating,
        desc,
        price,
        image,
        url,
        pdf
    )
}