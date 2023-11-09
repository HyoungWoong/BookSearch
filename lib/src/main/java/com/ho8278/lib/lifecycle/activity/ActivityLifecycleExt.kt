package com.ho8278.lib.lifecycle.activity

import androidx.lifecycle.lifecycleScope
import com.ho8278.lib.lifecycle.untilLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

fun <T> Flow<T>.untilLifecycle(lifecycleActivity: LifecycleActivity): Job {
    val lifecycleScope = ActivityLifecycleScope(lifecycleActivity, ActivityLifecycle.endFunction)

    return untilLifecycle(lifecycleScope)
        .launchIn(lifecycleActivity.lifecycleScope)
}