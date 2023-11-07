package com.ho8278.data.remote

import com.ho8278.lib.serialize.Serializer
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import java.lang.reflect.Type

internal class RequestBodyConverter<T>(
    private val type: Type,
    private val serializer: Serializer
) : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody? {
        val serializedString = serializer.serialize(value, type)
        return RequestBody.create(
            MediaType.parse("application/json; charset=UTF8"),
            serializedString
        )
    }
}