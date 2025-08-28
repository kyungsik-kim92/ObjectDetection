package com.example.presentation.ui.search.word

import com.example.model.api.DictionaryResponseItem

sealed class WordDetailUiState {
    data object Loading : WordDetailUiState()
    data class Success(val item: DictionaryResponseItem?, val isBookmark: Boolean = false) :
        WordDetailUiState()

    data object NotFound : WordDetailUiState()
    data class BookmarkUpdated(val isBookmark: Boolean) : WordDetailUiState()
    data class Error(val message: String) : WordDetailUiState()
}