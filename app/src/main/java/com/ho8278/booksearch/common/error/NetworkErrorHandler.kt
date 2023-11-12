package com.ho8278.booksearch.common.error

import android.content.Context
import android.widget.Toast
import com.ho8278.lib.error.ErrorHandler
import com.ho8278.lib.error.HandledException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class NetworkErrorHandler(
    private val delegate: ErrorHandler,
    private val appContext: Context,
) : ErrorHandler {
    override fun handle(throwable: Throwable) {
        if (throwable is HandledException) delegate.handle(throwable)

        when (throwable) {
            is HttpException, is IOException -> {
                Toast.makeText(appContext, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                delegate.handle(HandledException(throwable))
            }

            else -> delegate.handle(throwable)
        }
    }
}