package com.ho8278.booksearch.search

import android.os.Bundle
import com.ho8278.booksearch.databinding.ActivitySearchBinding
import com.ho8278.lib.lifecycle.activity.LifecycleActivity

class SearchActivity : LifecycleActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}