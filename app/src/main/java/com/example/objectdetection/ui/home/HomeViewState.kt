package com.example.objectdetection.ui.home

import com.example.objectdetection.base.ViewState
import com.example.objectdetection.data.model.BookmarkWord

sealed class HomeViewState : ViewState  {

    data class AddBookmark(val item: BookmarkWord) : HomeViewState()
    data class DeleteBookmark(val item: BookmarkWord) : HomeViewState()
    data class ShowToast(val message: String) : HomeViewState()
}