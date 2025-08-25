package com.example.presentation.ui.search.detect

import com.example.model.api.DictionaryResponseItem

sealed class SelectDetectionUiState {
    data object Loading : SelectDetectionUiState()
    data class Success(val word: DictionaryResponseItem) : SelectDetectionUiState()
    data object NotFound : SelectDetectionUiState()
    data class BookmarkUpdated(val isBookmark: Boolean) : SelectDetectionUiState()
    data class Error(val message: String) : SelectDetectionUiState()
}