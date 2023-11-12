package com.ho8278.booksearch.detail

import com.ho8278.data.repository.model.BooksResult
import com.ho8278.lib.lifecycle.viewmodel.LifecycleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : LifecycleViewModel() {
    val booksResult = MutableStateFlow<BooksResult?>(null)
}