package com.ho8278.booksearch.inject

import android.content.Context
import com.ho8278.booksearch.common.error.NetworkErrorHandler
import com.ho8278.booksearch.search.SearchErrorHandler
import com.ho8278.lib.error.LogErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ErrorModule {

    @Provides
    @Singleton
    fun provideNetworkError(@ApplicationContext context: Context): NetworkErrorHandler {
        return NetworkErrorHandler(LogErrorHandler(), context)
    }

    @Provides
    @Singleton
    fun provideSearchError(
        @ApplicationContext context: Context,
        networkErrorHandler: NetworkErrorHandler
    ): SearchErrorHandler {
        return SearchErrorHandler(networkErrorHandler, context)
    }
}