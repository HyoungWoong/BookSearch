package com.ho8278.lib.lifecycle

interface LifecycleScope {
    suspend fun whenEnd()
}