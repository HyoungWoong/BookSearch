package com.ho8278.booksearch.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.ho8278.booksearch.databinding.ActivitySearchBinding
import com.ho8278.lib.flowbinding.textChanges
import com.ho8278.lib.lifecycle.activity.LifecycleActivity
import com.ho8278.lib.lifecycle.activity.untilLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchActivity : LifecycleActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>()
    private val booksAdapter by lazy { BooksAdapter { println(it.toString()) } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initEdiText()
        initRecyclerView()
    }

    private fun initEdiText() {
        binding.search.textChanges()
            .debounce(500L)
            .onEach { viewModel.loadFirst(it) }
            .untilLifecycle(this)
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = booksAdapter

        viewModel.booksList
            .onEach {
                booksAdapter.submitList(it)
            }
            .untilLifecycle(this)
    }
}