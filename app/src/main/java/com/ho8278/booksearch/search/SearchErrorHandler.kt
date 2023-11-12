package com.ho8278.booksearch.search

import android.content.Context
import android.widget.Toast
import com.ho8278.lib.error.ErrorHandler
import com.ho8278.lib.error.HandledException

class SearchErrorHandler(
    private val delegate: ErrorHandler,
    private val appContext: Context,
) : ErrorHandler {
    override fun handle(throwable: Throwable) {
        if (throwable is HandledException) delegate.handle(throwable)

        if (throwable is IllegalArgumentException) {
            Toast.makeText(appContext, "검색어 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
        }
        delegate.handle(throwable)
    }
}