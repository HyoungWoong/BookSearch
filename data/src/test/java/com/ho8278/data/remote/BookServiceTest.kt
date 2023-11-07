package com.ho8278.data.remote

import com.ho8278.lib.serialize.KotlinSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.create

class BookServiceTest {

    companion object {

        private lateinit var bookService: BookService
        @BeforeClass
        @JvmStatic
        fun initRetrofit() {
            val kotlinSerializer = KotlinSerializer(Json)
            val converter = SerializerConverterFactory(kotlinSerializer)

            val okhttp = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.itbook.store/1.0/")
                .addConverterFactory(converter)
                .client(okhttp)
                .build()

            bookService = retrofit.create()
        }
    }

    @Test
    fun `searchBook API 가 정상적으로 호출된다`(): Unit = runBlocking {
        val result = bookService.searchBook("Android")
        println(result)
    }

    @Test
    fun `getBookDetail API 가 정상적으로 호출된다`(): Unit = runBlocking {
        val result = bookService.getBookDetail("9781617294136")
        println(result)
    }

    @Test
    fun `getBookDetail 결과 중에 pdf 가 없는 경우 emptyMap 으로 처리한다`(): Unit = runBlocking {
        val result = bookService.getBookDetail("9781449310370")
        assert(result.pdf.isEmpty())
    }
}