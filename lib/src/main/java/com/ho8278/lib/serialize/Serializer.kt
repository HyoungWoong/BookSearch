package com.ho8278.lib.serialize

import java.lang.reflect.Type

interface Serializer {
    fun <T> serialize(value: T, type: Type): String
    fun <T> deserialize(serializedString: String, type: Type): T?
}