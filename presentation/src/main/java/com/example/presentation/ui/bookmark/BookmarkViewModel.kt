package com.example.presentation.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getBookmarkWordListUseCase()
            .onStart { _uiState.value = BookmarkUiState.Loading }
            .map { bookmarkList ->
                when {
                    bookmarkList.isNullOrEmpty() -> BookmarkUiState.Empty
                    else -> BookmarkUiState.Success(bookmarkList)
                }
            }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }
}