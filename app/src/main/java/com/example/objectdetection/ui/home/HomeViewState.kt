package com.example.objectdetection.ui.home

import com.example.objectdetection.base.ViewEvent
import com.example.objectdetection.base.ViewState
import com.example.objectdetection.data.model.BookmarkWord


//sealed class HomeViewState : ViewState
sealed class HomeViewEvent : ViewEvent  {
    data class AddBookmark(val item: BookmarkWord) : HomeViewEvent()
    data class DeleteBookmark(val item: BookmarkWord) : HomeViewEvent()
    data class ShowToast(val message: String) : HomeViewEvent()
}