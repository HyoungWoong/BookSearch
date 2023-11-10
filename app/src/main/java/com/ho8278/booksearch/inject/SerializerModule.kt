package com.ho8278.booksearch.inject

import com.ho8278.lib.serialize.KotlinSerializer
import com.ho8278.lib.serialize.Serializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SerializerModule {

    @Provides
    @Singleton
    fun provideSerializer(): Serializer {
        return KotlinSerializer(Json)
    }
}