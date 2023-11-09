package com.ho8278.lib.lifecycle.viewmodel

enum class ViewModelLifecycle {
    CREATE,
    CLEAR;

    companion object {
        val endFunction:(ViewModelLifecycle) -> ViewModelLifecycle = {
            when(it) {
                CREATE -> CLEAR
                CLEAR -> throw IllegalStateException("Lifecycle already end.")
            }
        }
    }
}