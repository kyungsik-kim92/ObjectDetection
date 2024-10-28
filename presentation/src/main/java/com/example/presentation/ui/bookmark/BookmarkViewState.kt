package com.example.presentation.ui.bookmark

import com.example.model.BookmarkWord
import com.example.presentation.base.ViewState

data class BookmarkViewState(
    val bookmarkList: List<BookmarkWord> = emptyList()
) : ViewState

//    data class GetBookmarkList(val list: List<BookmarkWord>) : BookmarkViewState()
//    object EmptyBookmarkList : BookmarkViewState()
//    data class ShowToast(val message: String) : BookmarkViewState()
