package com.ho8278.lib.lifecycle

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun <T> Flow<T>.untilLifecycle(lifecycleScope: LifecycleScope) = callbackFlow<T> {
    val subRoutine = launch {
        this@untilLifecycle.collect {
            if(this@callbackFlow.isActive) this@callbackFlow.send(it)
        }
    }

    launch {
        lifecycleScope.until()
        close()
    }

    awaitClose { subRoutine.cancel() }
}