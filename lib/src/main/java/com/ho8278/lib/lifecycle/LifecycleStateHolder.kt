package com.ho8278.lib.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LifecycleStateHolder<T>(startState: T) : LifecycleHolder<T> {

    private val lifecycleHolder = MutableStateFlow(startState)

    override fun currentLifecycle(): T = lifecycleHolder.value

    override fun getLifecycleFlow(): Flow<T> = lifecycleHolder

    fun onLifecycle(value: T) {
        lifecycleHolder.tryEmit(value)
    }
}