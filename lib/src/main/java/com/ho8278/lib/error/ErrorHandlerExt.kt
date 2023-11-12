package com.ho8278.lib.error

import kotlinx.coroutines.CoroutineExceptionHandler

fun ErrorHandler.forCoroutine(): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
        handle(throwable)
    }
}