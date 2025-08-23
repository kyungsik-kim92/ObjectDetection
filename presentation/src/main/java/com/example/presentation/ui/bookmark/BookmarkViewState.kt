package com.example.presentation.ui.bookmark

import com.example.model.BookmarkWord

sealed class BookmarkUiState {
    data object Loading : BookmarkUiState()
    data class Success(val bookmarkList: List<BookmarkWord>) : BookmarkUiState()
    data object Empty : BookmarkUiState()
}

sealed class BookmarkUiEvent {
    data class ShowToast(val message: String) : BookmarkUiEvent()
    data object BookmarkDeleted : BookmarkUiEvent()
}