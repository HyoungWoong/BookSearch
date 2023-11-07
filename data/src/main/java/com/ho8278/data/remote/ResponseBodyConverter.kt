package com.ho8278.data.remote

import com.ho8278.lib.serialize.Serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

internal class ResponseBodyConverter<T>(
    private val type: Type,
    private val serializer: Serializer
) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        return serializer.deserialize(value.string(), type)
    }
}