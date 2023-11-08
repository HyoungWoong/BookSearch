package com.ho8278.data.repository

import com.ho8278.data.remote.BookService
import com.ho8278.data.remote.SerializerConverterFactory
import com.ho8278.lib.serialize.KotlinSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.create

class BookRepositoryTest {

    companion object {
        private lateinit var repository: BookRepository

        @BeforeClass
        @JvmStatic
        fun initRepository() {
            val kotlinSerializer = KotlinSerializer(Json)
            val converter = SerializerConverterFactory(kotlinSerializer)

            val okhttp = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.itbook.store/1.0/")
                .addConverterFactory(converter)
                .client(okhttp)
                .build()

            val bookService: BookService = retrofit.create()

            repository = BookRepository(bookService)
        }
    }

    @Test
    fun `or 연산자가 있는 경우 검색어가 포함된 모든 도서목록을 가져온다`() = runBlocking {
        val query = "android|java"

        val result = repository.searchBook(query)

        val containsAll = result.books.all {
            it.title.lowercase().contains("android") || it.title.lowercase().contains("java")
        }

        assert(containsAll)
    }

    @Test
    fun `검색어가 3개 이상이고 연산자가 2개 이상인 경우 에러를 던진다`() = runBlocking {
        val query = "android|java|kotlin"

        try {
            repository.searchBook(query)
            assert(false)
        } catch (ignore: IllegalArgumentException) {
            assert(true)
        }
    }

    @Test
    fun `검색어가 1개이고 연산자가 1개인 경우 연산자를 무시한다`() = runBlocking {
        val query1 = "android|"
        val query2 = "|android"
        println(query1.split("|"))

        val result1 = repository.searchBook(query1)
        val result2 = repository.searchBook(query2)

        val containsAllAndroid1 = result1.books.all { it.title.lowercase().contains("android") }
        val containsAllAndroid2 = result2.books.all { it.title.lowercase().contains("android") }

        assert(containsAllAndroid1 && containsAllAndroid2)
    }

    @Test
    fun `not 연산자가 있는 경우 다음 검색어의 결과가 제외된 도서목록을 가져온다`() = runBlocking {
        val query = "android-java"

        val result = repository.searchBook(query)

        val containsAllAndroid = result.books.all { it.title.contains("android") }
        val excludeAllJava = result.books.all { !it.title.contains("java") }

        assert(containsAllAndroid && excludeAllJava)
    }
}