package com.ho8278.lib.lifecycle.viewmodel

import androidx.lifecycle.ViewModel
import com.ho8278.lib.lifecycle.LifecycleHolder
import com.ho8278.lib.lifecycle.LifecycleStateHolder
import kotlinx.coroutines.flow.Flow

open class LifecycleViewModel : ViewModel(), LifecycleHolder<ViewModelLifecycle> {

    private val lifecycleStateHolder = LifecycleStateHolder(ViewModelLifecycle.CREATE)
    override fun currentLifecycle(): ViewModelLifecycle = lifecycleStateHolder.currentLifecycle()

    override fun getLifecycleFlow(): Flow<ViewModelLifecycle> = lifecycleStateHolder.getLifecycleFlow()

    override fun onCleared() {
        super.onCleared()
        lifecycleStateHolder.onLifecycle(ViewModelLifecycle.CLEAR)
    }
}