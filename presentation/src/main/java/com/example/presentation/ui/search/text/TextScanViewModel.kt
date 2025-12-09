package com.example.presentation.ui.search.text

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TextScanViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<TextScanUiState>(TextScanUiState.Idle)
    val uiState: StateFlow<TextScanUiState> = _uiState.asStateFlow()

    private val _recognizedWords = MutableStateFlow<List<String>>(emptyList())
    val recognizedWords: StateFlow<List<String>> = _recognizedWords.asStateFlow()

    fun onTextRecognized(text: String) {
        if (text.isBlank()) {
            _uiState.value = TextScanUiState.Error("텍스트를 찾을 수 없습니다")
            return
        }

        val words = text.split(Regex("[^a-zA-Z]+"))
            .filter { it.length >= 2 }
            .map { it.lowercase() }
            .distinct()

        if (words.isEmpty()) {
            _uiState.value = TextScanUiState.Error("영어 단어를 찾을 수 없습니다")
            return
        }

        _recognizedWords.value = words
        _uiState.value = TextScanUiState.Success(text)
    }

    fun onWordSelected(word: String) {
        _uiState.value = TextScanUiState.WordSelected(word)
    }

    fun onError(message: String) {
        _uiState.value = TextScanUiState.Error(message)
    }

    fun resetState() {
        _uiState.value = TextScanUiState.Idle
    }
}