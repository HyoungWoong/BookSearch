package com.ho8278.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteSearchResult(val error:String, val total: Int, val page: Int = 0, val books: List<RemoteBook>)
