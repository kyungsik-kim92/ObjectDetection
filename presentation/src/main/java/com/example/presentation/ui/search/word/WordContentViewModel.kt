package com.example.presentation.ui.search.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WordContentViewModel @Inject constructor(
    searchWordRepository: com.example.domain.repo.SearchWordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WordContentUiState>(WordContentUiState.Loading)
    val uiState: StateFlow<WordContentUiState> = _uiState.asStateFlow()

    val inputTextFlow = MutableStateFlow("")

    init {
        inputTextFlow.onEach { searchText ->
            if (searchText.isEmpty()) {
                _uiState.value = WordContentUiState.Empty
            } else {
                val searchList = searchWordRepository.excelList.first()
                    .filter { it.word.length >= searchText.length }
                    .filter { it.word.substring(searchText.indices).contains(searchText) }

                _uiState.value = if (searchList.isEmpty()) {
                    WordContentUiState.Empty
                } else {
                    WordContentUiState.Success(searchList)
                }
            }
        }.launchIn(viewModelScope)
    }
}