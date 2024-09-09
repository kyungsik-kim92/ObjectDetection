package com.example.presentation.ui.search.word

import com.example.presentation.base.ViewState
import com.example.presentation.ui.adapter.WordItem

sealed class WordContentViewState : ViewState {
    data class GetSearchResult(val list: List<WordItem>) : WordContentViewState()
    object EmptyResult : WordContentViewState()
    data class ShowToast(val message: String) : WordContentViewState()

    object ShowProgress : WordContentViewState()
    object HideProgress : WordContentViewState()
}