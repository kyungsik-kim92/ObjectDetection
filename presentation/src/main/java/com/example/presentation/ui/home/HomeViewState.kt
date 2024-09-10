package com.example.presentation.ui.home

import com.example.domain.model.BookmarkWord
import com.example.presentation.base.ViewEvent


//sealed class HomeViewState : ViewState
sealed class HomeViewEvent : ViewEvent {
    data class AddBookmark(val item: BookmarkWord) : HomeViewEvent()
    data class DeleteBookmark(val item: BookmarkWord) : HomeViewEvent()
    data class ShowToast(val message: String) : HomeViewEvent()
}