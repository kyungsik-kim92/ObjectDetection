package com.example.presentation.ui.search.word

import com.example.model.WordItem
import com.example.presentation.base.ViewState


sealed class WordContentViewState : ViewState {
    data class GetSearchResult(val list: List<WordItem>) : WordContentViewState()
    object EmptyResult : WordContentViewState()
    data class ShowToast(val message: String) : WordContentViewState()

    object ShowProgress : WordContentViewState()
    object HideProgress : WordContentViewState()
}