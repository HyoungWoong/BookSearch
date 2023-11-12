package com.ho8278.booksearch.search

import com.ho8278.data.BookRepository
import com.ho8278.data.repository.model.Book
import com.ho8278.data.repository.model.BooksResult
import com.ho8278.data.repository.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private lateinit var loadMoreViewModel: SearchViewModel
    private val repository = FakeBookRepository()
    private val loadMoreRepository = LoadMoreRepository()

    @Before
    fun initViewModel() {
        Dispatchers.setMain(StandardTestDispatcher())
        val searchErrorHandler = mock(SearchErrorHandler::class.java)

        viewModel = SearchViewModel(repository, searchErrorHandler)
        loadMoreViewModel = SearchViewModel(loadMoreRepository, searchErrorHandler)
    }

    @After
    fun resetMain() {
        Dispatchers.resetMain()
    }

    @Test
    fun `빈 쿼리를 요청하면 빈 리스트를 보여준다`(): Unit = runTest {
        viewModel.loadFirst("")
        advanceUntilIdle()

        assert(viewModel.booksList.first().isEmpty())
    }

    @Test
    fun `최대 갯수만큼 도서목록을 가져올 경우 loadMore 는 항상 무시된다`() = runTest {
        viewModel.loadFirst("AAA")
        advanceUntilIdle()
        val firstResult = viewModel.books.value
        assert(firstResult != null)

        viewModel.loadMore()
        advanceUntilIdle()

        val secondResult = viewModel.books.value
        assert(secondResult != null)

        assert(firstResult!!.books == secondResult!!.books)
    }

    @Test
    fun `최대 갯수만큼 도서목록을 가져오지 않은 경우 loadMore 는 page 를 증가시킨다`() = runTest {
        loadMoreViewModel.loadFirst("AAA")
        advanceUntilIdle()
        val firstResult = loadMoreViewModel.books.value
        assert(firstResult != null)

        loadMoreViewModel.loadMore()
        advanceUntilIdle()

        val secondResult = loadMoreViewModel.books.value
        assert(secondResult != null)

        assert(firstResult!!.page + 1 == secondResult!!.page)
    }
}

class FakeBookRepository : BookRepository {
    override suspend fun searchBook(query: String, page: Int): SearchResult {
        return SearchResult(
            5,
            page,
            listOf(
                Book("111", "111", "1111111111111", "111", "111", "111"),
                Book("222", "222", "2222222222222", "222", "222", "222"),
                Book("333", "333", "3333333333333", "333", "333", "333"),
                Book("444", "444", "4444444444444", "444", "444", "444"),
                Book("555", "555", "5555555555555", "555", "555", "555"),
            )
        )
    }

    override suspend fun getBookDetail(isbn: String): BooksResult {
        TODO("Not yet implemented")
    }
}

class LoadMoreRepository : BookRepository {
    override suspend fun searchBook(query: String, page: Int): SearchResult {
        return SearchResult(
            7,
            page,
            listOf(
                Book("111", "111", "1111111111111", "111", "111", "111"),
                Book("222", "222", "2222222222222", "222", "222", "222"),
                Book("333", "333", "3333333333333", "333", "333", "333"),
                Book("444", "444", "4444444444444", "444", "444", "444"),
                Book("555", "555", "5555555555555", "555", "555", "555"),
            )
        )
    }

    override suspend fun getBookDetail(isbn: String): BooksResult {
        TODO("Not yet implemented")
    }
}