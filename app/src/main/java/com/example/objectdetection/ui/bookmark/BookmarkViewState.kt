package com.example.objectdetection.ui.bookmark

import com.example.objectdetection.base.ViewState
import com.example.objectdetection.data.model.BookmarkWord

sealed class BookmarkViewState : ViewState {

    data class GetBookmarkList(val list: List<BookmarkWord>) : BookmarkViewState()
    object EmptyBookmarkList : BookmarkViewState()
    data class ShowToast(val message: String) : BookmarkViewState()
}