package com.example.presentation.ui.search.detect

import com.example.data.network.response.DictionaryResponseItem
import com.example.presentation.base.ViewState

sealed class SelectDetectionViewState : ViewState {
    data class GetSearchWord(val word: DictionaryResponseItem) : SelectDetectionViewState()
    data class BookmarkState(val isBookmark: Boolean) : SelectDetectionViewState()
    data class ShowToast(val message: String) : SelectDetectionViewState()

    object NotSearchWord : SelectDetectionViewState()

    object ShowProgress : SelectDetectionViewState()
    object HideProgress : SelectDetectionViewState()
}