package com.ho8278.booksearch.inject

import com.ho8278.data.BookRepository
import com.ho8278.data.remote.BookService
import com.ho8278.data.repository.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideBookRepository(bookService: BookService): BookRepository {
        return BookRepositoryImpl(bookService)
    }
}