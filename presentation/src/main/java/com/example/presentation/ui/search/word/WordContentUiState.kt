package com.example.presentation.ui.search.word

import com.example.model.WordItem

sealed class WordContentUiState {
    data object Loading : WordContentUiState()
    data class Success(val searchList: List<WordItem>) : WordContentUiState()
    data object Empty : WordContentUiState()
}