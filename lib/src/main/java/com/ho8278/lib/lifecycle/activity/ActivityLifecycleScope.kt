package com.ho8278.lib.lifecycle.activity

import com.ho8278.lib.lifecycle.LifecycleHolder
import com.ho8278.lib.lifecycle.LifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ActivityLifecycleScope(
    private val lifecycleStateHolder: LifecycleHolder<ActivityLifecycle>,
    private val endFunction: (ActivityLifecycle) -> ActivityLifecycle,
) : LifecycleScope {
    override suspend fun whenEnd() {
        val currentLifecycle = lifecycleStateHolder.currentLifecycle()
        val endState = endFunction(currentLifecycle)

        coroutineScope {
            launch {
                lifecycleStateHolder.getLifecycle()
                    .collect {
                        if (endState <= it) this.cancel()
                    }
            }
        }
    }
}