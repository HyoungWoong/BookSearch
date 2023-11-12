package com.ho8278.lib.error

class LogErrorHandler:ErrorHandler {
    override fun handle(throwable: Throwable) {
        throwable.printStackTrace()
    }
}