package com.ho8278.booksearch.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.ho8278.booksearch.databinding.ActivitySearchBinding
import com.ho8278.booksearch.detail.DetailActivity
import com.ho8278.data.repository.model.BooksResult
import com.ho8278.lib.flowbinding.textChanges
import com.ho8278.lib.lifecycle.activity.LifecycleActivity
import com.ho8278.lib.lifecycle.activity.untilLifecycle
import com.ho8278.lib.serialize.Serializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : LifecycleActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>()
    private val booksAdapter by lazy { BooksAdapter { viewModel.onClickItem(it) } }

    @Inject
    lateinit var serializer: Serializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initEdiText()
        initRecyclerView()
        initSignal()
    }

    private fun initEdiText() {
        if (viewModel.searchText.value.isNotEmpty()) {
            binding.search.setText(viewModel.searchText.value)
        }

        binding.search.textChanges()
            .debounce(500L)
            .onEach { viewModel.loadFirst(it) }
            .untilLifecycle(this)
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = booksAdapter

        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val lastVisiblePosition =
                        (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

                    val loadPosition = booksAdapter.itemCount - 3

                    if (lastVisiblePosition != null && lastVisiblePosition > loadPosition) {
                        viewModel.loadMore()
                    }
                }
            }
        })

        viewModel.booksList
            .onEach {
                booksAdapter.submitList(it)
            }
            .untilLifecycle(this)
    }

    private fun initSignal() {
        viewModel.signals
            .filterIsInstance<SearchViewModel.OpenBookDetail>()
            .onEach {
                val intent = Intent(this, DetailActivity::class.java)
                val bookResultString = serializer.serialize(it.booksResult, BooksResult::class.java)
                intent.putExtra(DetailActivity.KEY_BOOK_RESULT, bookResultString)

                startActivity(intent)
            }
            .untilLifecycle(this)
    }
}