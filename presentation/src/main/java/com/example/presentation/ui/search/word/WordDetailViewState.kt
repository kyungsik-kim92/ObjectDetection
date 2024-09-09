package com.example.presentation.ui.search.word

import com.example.presentation.base.ViewState
import com.example.objectdetection.network.response.DictionaryResponseItem

sealed class WordDetailViewState : ViewState {
    data class GetSearchWord(val word: DictionaryResponseItem) : WordDetailViewState()
    data class BookmarkState(val isBookmark: Boolean) : WordDetailViewState()
    data class ShowToast(val message: String) : WordDetailViewState()

    object NotSearchWord : WordDetailViewState()

    object ShowProgress : WordDetailViewState()
    object HideProgress : WordDetailViewState()
}