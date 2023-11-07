package com.ho8278.lib.serialize

import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import java.lang.reflect.Type

class KotlinSerializer(private val stringFormat: StringFormat) : Serializer {
    override fun <T> serialize(value: T, type: Type): String {
        val serializer = stringFormat.serializersModule.serializer(type)
        return if(value == null) ""
        else stringFormat.encodeToString(serializer, value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> deserialize(serializedString: String, type: Type): T? {
        val deserializer = stringFormat.serializersModule.serializer(type)
        return try {
            stringFormat.decodeFromString(deserializer, serializedString) as T
        } catch (exception: Exception) {
            null
        }
    }
}