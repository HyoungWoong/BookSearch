package com.ho8278.booksearch.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.ho8278.booksearch.common.signal.BaseSignal
import com.ho8278.data.BookRepository
import com.ho8278.data.repository.model.Book
import com.ho8278.data.repository.model.BooksResult
import com.ho8278.data.repository.model.SearchResult
import com.ho8278.lib.error.forCoroutine
import com.ho8278.lib.lifecycle.viewmodel.LifecycleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val errorHandler: SearchErrorHandler,
) : LifecycleViewModel() {

    @VisibleForTesting
    val books = MutableStateFlow<SearchResult?>(null)

    val searchText = MutableStateFlow("")

    val signals = MutableSharedFlow<BaseSignal>()

    val booksList = books.map { searchResult ->
        searchResult?.books.orEmpty()
            .flatMap { listOf(ItemHolder.BookEntry(it), ItemHolder.Divider) }
            .dropLast(1)
    }

    fun loadFirst(query: String) {
        viewModelScope.launch(errorHandler.forCoroutine()) {
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
        viewModelScope.launch(errorHandler.forCoroutine()) {
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

    fun onClickItem(book: Book) {
        viewModelScope.launch(errorHandler.forCoroutine()) {
            val bookResult = bookRepository.getBookDetail(book.isbn13)
            signals.emit(OpenBookDetail(bookResult))
        }
    }

    data class OpenBookDetail(val booksResult: BooksResult) : BaseSignal
}