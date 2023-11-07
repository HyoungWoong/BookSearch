package com.ho8278.data.remote.model

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