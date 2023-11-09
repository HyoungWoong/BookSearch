package com.ho8278.lib.lifecycle

import kotlinx.coroutines.flow.Flow

interface LifecycleHolder<T> {
    fun currentLifecycle(): T
    fun getLifecycle(): Flow<T>
}