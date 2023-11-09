package com.ho8278.lib.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LifecycleStateHolder<T>(startState: T) : LifecycleHolder<T> {

    private val lifecycleHolder = MutableStateFlow(startState)

    override fun currentLifecycle(): T = lifecycleHolder.value

    override fun getLifecycle(): Flow<T> = lifecycleHolder

    fun onLifecycle(value: T) {
        lifecycleHolder.tryEmit(value)
    }
}