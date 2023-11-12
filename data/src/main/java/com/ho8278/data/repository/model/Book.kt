package com.ho8278.data.repository.model

import com.ho8278.data.remote.model.RemoteBook
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val title: String,
    val subtitle: String,
    val isbn13: String,
    val price: String,
    val image: String,
    val url: String
)

internal fun RemoteBook.toBook(): Book {
    return Book(title, subtitle, isbn13, price, image, url)
}