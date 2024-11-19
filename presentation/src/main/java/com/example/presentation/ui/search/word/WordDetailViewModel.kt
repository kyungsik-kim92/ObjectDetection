package com.example.presentation.ui.search.word

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.AddWordUseCase
import com.example.domain.usecase.firebase.DeleteWordUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.word.SearchWordUseCase
import com.example.model.WordItem
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    searchWordUseCase: SearchWordUseCase,
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase,
    savedStateHandle: SavedStateHandle,
    private val addWordUseCase: AddWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
) : BaseViewModel() {


    private val getRouteItem = savedStateHandle.get<WordItem>(ARG_WORD)

    init {
        combine(
            searchWordUseCase(getRouteItem?.word.orEmpty()),
            getBookmarkWordListUseCase()
        ) { item, list ->
            onChangedViewState(
                WordDetailViewState(
                    item = item.first(),
                    isBookmark = list.any { it.word == getRouteItem?.word.orEmpty() })
            )
        }.launchIn(viewModelScope)
    }

    fun toggleBookmark(state: Boolean) {
        getRouteItem?.let { item ->
            if (state) {
                addWordUseCase(item.toBookmarkWord()).launchIn(viewModelScope)
            } else {
                deleteWordUseCase(item.toBookmarkWord()).launchIn(viewModelScope)
            }
        }
    }
}
