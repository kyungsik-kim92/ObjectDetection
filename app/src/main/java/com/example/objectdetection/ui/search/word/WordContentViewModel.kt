package com.example.objectdetection.ui.search.word

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.SearchWordRepository
import com.example.objectdetection.ui.adapter.WordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WordContentViewModel @Inject constructor(
    app: Application,
    searchWordRepository: SearchWordRepository
) : BaseViewModel(app) {

    private val excelList = mutableListOf<WordItem>()

    init {
        searchWordRepository.excelList.onEach {
            excelList.addAll(it.map { it.toWordItem() })
        }.launchIn(viewModelScope)
    }

    fun searchWord(word: String) {
        viewStateChanged(WordContentViewState.ShowProgress)

        val toWordItemList = excelList.filter { it.word.length >= word.length }
            .filter { it.word.substring(word.indices).contains(word) }

        if (toWordItemList.isEmpty()) {
            viewStateChanged(WordContentViewState.EmptyResult)
            viewStateChanged(WordContentViewState.HideProgress)
        } else {
            viewStateChanged(WordContentViewState.GetSearchResult(toWordItemList))
            viewStateChanged(WordContentViewState.HideProgress)
        }
    }

}