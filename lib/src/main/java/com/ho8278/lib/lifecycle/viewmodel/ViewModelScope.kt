package com.ho8278.lib.lifecycle.viewmodel

import com.ho8278.lib.lifecycle.LifecycleHolder
import com.ho8278.lib.lifecycle.LifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class ViewModelScope(
    private val lifecycleStateHolder: LifecycleHolder<ViewModelLifecycle>,
    private val endFunction: (ViewModelLifecycle) -> ViewModelLifecycle,
) : LifecycleScope {
    override suspend fun until() {
        val currentLifecycle = lifecycleStateHolder.currentLifecycle()
        val endState = endFunction(currentLifecycle)

        coroutineScope {
            launch {
                lifecycleStateHolder.getLifecycleFlow()
                    .collect {
                        if (endState <= it) this.cancel()
                    }
            }
        }
    }
}