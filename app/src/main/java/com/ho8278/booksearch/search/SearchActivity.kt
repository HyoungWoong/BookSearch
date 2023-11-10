package com.ho8278.booksearch.search

import android.os.Bundle
import androidx.activity.viewModels
import com.ho8278.booksearch.databinding.ActivitySearchBinding
import com.ho8278.lib.lifecycle.activity.LifecycleActivity
import com.ho8278.lib.lifecycle.activity.untilLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchActivity : LifecycleActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.books
            .onEach {
                println(it?.books?.size)
                viewModel.loadMore()
            }
            .untilLifecycle(this)

        viewModel.loadFirst("android")
    }
}