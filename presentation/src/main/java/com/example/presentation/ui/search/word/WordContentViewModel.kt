package com.example.presentation.ui.search.word

import androidx.lifecycle.viewModelScope
import com.example.domain.repo.SearchWordRepository
import com.example.model.WordItem
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WordContentViewModel @Inject constructor(
    searchWordRepository: com.example.domain.repo.SearchWordRepository
) : BaseViewModel() {

    private val excelList = mutableListOf<WordItem>()

    init {
//        searchWordRepository.excelList.onEach {
//            excelList.addAll(it.map { it.toWordItem() })
//        }.launchIn(viewModelScope)
    }

    fun searchWord(word: String) {
        onChangedViewState(WordContentViewState.ShowProgress)

        val toWordItemList = excelList.filter { it.word.length >= word.length }
            .filter { it.word.substring(word.indices).contains(word) }

        if (toWordItemList.isEmpty()) {
            onChangedViewState(WordContentViewState.EmptyResult)
            onChangedViewState(WordContentViewState.HideProgress)
        } else {
            onChangedViewState(WordContentViewState.GetSearchResult(toWordItemList))
            onChangedViewState(WordContentViewState.HideProgress)
        }
    }

}