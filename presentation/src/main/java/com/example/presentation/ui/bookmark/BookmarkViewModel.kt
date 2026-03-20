package com.example.presentation.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = BookmarkUiState.Loading

            getBookmarkWordListUseCase().collect { bookmarkList ->
                _uiState.value = if (bookmarkList.isEmpty()) {
                    BookmarkUiState.Empty
                } else {
                    BookmarkUiState.Success(bookmarkList)
                }
            }
        }
    }
}