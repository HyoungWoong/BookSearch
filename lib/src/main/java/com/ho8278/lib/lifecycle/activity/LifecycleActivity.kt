package com.ho8278.lib.lifecycle.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ho8278.lib.lifecycle.LifecycleHolder
import com.ho8278.lib.lifecycle.LifecycleStateHolder
import kotlinx.coroutines.flow.Flow

open class LifecycleActivity : AppCompatActivity(), LifecycleHolder<ActivityLifecycle> {

    private val lifecycleStateHolder = LifecycleStateHolder(ActivityLifecycle.CREATE)
    override fun currentLifecycle(): ActivityLifecycle = lifecycleStateHolder.currentLifecycle()

    override fun getLifecycleFlow(): Flow<ActivityLifecycle> = lifecycleStateHolder.getLifecycleFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleStateHolder.onLifecycle(ActivityLifecycle.CREATE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleStateHolder.onLifecycle(ActivityLifecycle.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleStateHolder.onLifecycle(ActivityLifecycle.RESUME)
    }

    override fun onPause() {
        super.onPause()
        lifecycleStateHolder.onLifecycle(ActivityLifecycle.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        lifecycleStateHolder.onLifecycle(ActivityLifecycle.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleStateHolder.onLifecycle(ActivityLifecycle.DESTROY)
    }
}