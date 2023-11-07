package com.ho8278.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteBook(
    val title: String,
    val subtitle: String,
    val isbn13: String,
    val price: String,
    val image: String,
    val url: String
)