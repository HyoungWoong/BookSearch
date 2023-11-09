package com.ho8278.lib.lifecycle.viewmodel

import androidx.lifecycle.viewModelScope
import com.ho8278.lib.lifecycle.untilLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

fun <T> Flow<T>.untilLifecycle(lifecycleViewModel: LifecycleViewModel): Job {
    val lifecycleScope = ViewModelScope(lifecycleViewModel, ViewModelLifecycle.endFunction)

    return untilLifecycle(lifecycleScope)
        .launchIn(lifecycleViewModel.viewModelScope)
}