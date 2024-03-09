package com.example.objectdetection.ui.search.word

import com.example.objectdetection.base.ViewState
import com.example.objectdetection.ui.adapter.WordItem

sealed class WordContentViewState : ViewState {
    data class GetSearchResult(val list: List<WordItem>) : WordContentViewState()
    object EmptyResult : WordContentViewState()
    data class ShowToast(val message: String) : WordContentViewState()

    object ShowProgress : WordContentViewState()
    object HideProgress : WordContentViewState()
}