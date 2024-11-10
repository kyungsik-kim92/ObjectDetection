package com.example.presentation.ui.search.word

import androidx.lifecycle.viewModelScope
import com.example.model.WordItem
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class WordContentViewModel @Inject constructor(
    searchWordRepository: com.example.domain.repo.SearchWordRepository
) : BaseViewModel() {

    val inputTextFlow = MutableStateFlow("")

    init {
        inputTextFlow.debounce(400L).onStart {
            onChangedViewState(WordContentViewState(visibleProgress = true))
        }.onEach { searchText ->
            val searchList = searchWordRepository.excelList.first()
                .filter { it.word.length >= searchText.length }
                .filter { it.word.substring(searchText.indices).contains(searchText) }
            onChangedViewState(WordContentViewState(searchList = searchList))
        }.launchIn(viewModelScope)
    }
}