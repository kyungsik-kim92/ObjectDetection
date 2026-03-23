package com.example.presentation.ui.home

import com.example.domain.model.BookmarkWord


sealed class HomeUiEvent {
    data class AddBookmark(val item: BookmarkWord) : HomeUiEvent()
    data class DeleteBookmark(val item: BookmarkWord) : HomeUiEvent()
}