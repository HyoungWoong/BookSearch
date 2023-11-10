package com.ho8278.booksearch.search

import androidx.lifecycle.viewModelScope
import com.ho8278.data.BookRepository
import com.ho8278.data.repository.model.SearchResult
import com.ho8278.lib.lifecycle.viewmodel.LifecycleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : LifecycleViewModel() {

    val books = MutableStateFlow<SearchResult?>(null)

    val searchText = MutableStateFlow("")

    fun loadFirst(query: String) {
        viewModelScope.launch {
            searchText.emit(query)
            if (query.isEmpty()) {
                books.emit(null)
            } else {
                val searchResult = bookRepository.searchBook(query)
                books.emit(searchResult)
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            if (books.value == null) return@launch

            val currentBooks = books.value!!
            val totalItems = currentBooks.total
            val currentItems = currentBooks.books.size
            val currentPage = currentBooks.page

            if (currentItems < totalItems) {
                val currentQuery = searchText.value
                val searchResult = bookRepository.searchBook(currentQuery, currentPage + 1)

                val mergedBookList = currentBooks.books + searchResult.books
                val newBooks = currentBooks.copy(page = searchResult.page, books = mergedBookList)
                books.emit(newBooks)
            } else {
                return@launch
            }
        }
    }
}