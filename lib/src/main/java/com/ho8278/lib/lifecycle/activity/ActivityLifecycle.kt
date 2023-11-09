package com.ho8278.lib.lifecycle.activity

enum class ActivityLifecycle {
    CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    DESTROY;

    companion object {
        val endFunction: (ActivityLifecycle) -> ActivityLifecycle = {
            when(it) {
                CREATE -> DESTROY
                START -> STOP
                RESUME -> PAUSE
                PAUSE -> STOP
                STOP -> DESTROY
                DESTROY -> throw IllegalStateException("Lifecycle already end.")
            }
        }
    }
}