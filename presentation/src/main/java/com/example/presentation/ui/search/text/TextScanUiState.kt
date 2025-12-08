package com.example.presentation.ui.search.text

sealed class TextScanUiState {
    data object Idle : TextScanUiState()
    data object Processing : TextScanUiState()
    data class Success(val text: String) : TextScanUiState()
    data class WordSelected(val word: String) : TextScanUiState()
    data class Error(val message: String) : TextScanUiState()
}