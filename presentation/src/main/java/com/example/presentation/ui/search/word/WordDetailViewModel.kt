package com.example.presentation.ui.search.word

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.AddWordUseCase
import com.example.domain.usecase.firebase.DeleteWordUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.word.SearchWordUseCase
import com.example.model.WordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    searchWordUseCase: SearchWordUseCase,
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase,
    savedStateHandle: SavedStateHandle,
    private val addWordUseCase: AddWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WordDetailUiState>(WordDetailUiState.Loading)
    val uiState: StateFlow<WordDetailUiState> = _uiState.asStateFlow()

    private val getRouteItem = savedStateHandle.get<WordItem>(ARG_WORD)

    init {
        _uiState.value = WordDetailUiState.Loading

        combine(
            searchWordUseCase(getRouteItem?.word.orEmpty()),
            getBookmarkWordListUseCase()
        ) { searchResult, bookmarkList ->
            if (searchResult.isNotEmpty()) {
                val item = searchResult.first()
                val isBookmark = bookmarkList.any { it.word == getRouteItem?.word.orEmpty() }
                _uiState.value = WordDetailUiState.Success(item, isBookmark)
            } else {
                _uiState.value = WordDetailUiState.NotFound
            }
        }.launchIn(viewModelScope)
    }

    fun toggleBookmark(state: Boolean) {
        getRouteItem?.let { item ->
            if (state) {
                addWordUseCase(item.toBookmarkWord()).onEach { isSuccess ->
                    if (isSuccess) {
                        _uiState.value = WordDetailUiState.BookmarkUpdated(true)
                    } else {
                        _uiState.value = WordDetailUiState.BookmarkUpdated(false)
                    }
                }
                    .launchIn(viewModelScope)

            } else {
                deleteWordUseCase(item.toBookmarkWord())
                    .onEach { isSuccess ->
                        if (isSuccess) {
                            _uiState.value = WordDetailUiState.BookmarkUpdated(false)
                        } else {
                            _uiState.value = WordDetailUiState.BookmarkUpdated(true)
                        }
                    }
                    .launchIn(viewModelScope)
            }

//            val currentState = _uiState.value
//            if (currentState is WordDetailUiState.Success) {
//                _uiState.value = WordDetailUiState.BookmarkUpdated(state)
//            }
        }
    }
}
