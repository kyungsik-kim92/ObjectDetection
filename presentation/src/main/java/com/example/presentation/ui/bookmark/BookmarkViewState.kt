package com.example.presentation.ui.bookmark

import com.example.model.BookmarkWord
import com.example.presentation.base.ViewState

sealed class BookmarkViewState : ViewState {

    data class GetBookmarkList(val list: List<BookmarkWord>) : BookmarkViewState()
    object EmptyBookmarkList : BookmarkViewState()
    data class ShowToast(val message: String) : BookmarkViewState()
}