package com.example.presentation.ui.search.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.AddWordUseCase
import com.example.domain.usecase.firebase.DeleteWordUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.word.SearchWordUseCase
import com.example.model.BookmarkWord
import com.example.model.WordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val searchWordUseCase: SearchWordUseCase,
    private val getBookmarkWordListUseCase: GetBookmarkWordListUseCase,
    private val addWordUseCase: AddWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WordDetailUiState>(WordDetailUiState.Loading)
    val uiState: StateFlow<WordDetailUiState> = _uiState.asStateFlow()

    private var wordItem: WordItem? = null


    fun setWordItem(wordItem: WordItem) {
        this.wordItem = wordItem
        _uiState.value = WordDetailUiState.Loading
        combine(
            searchWordUseCase(wordItem.word),
            getBookmarkWordListUseCase()
        ) { searchResult, bookmarkList ->
            if (searchResult.isNotEmpty()) {
                val item = searchResult.first()
                val isBookmark = bookmarkList.any { it.word == wordItem.word }
                _uiState.value = WordDetailUiState.Success(item, isBookmark)
            } else {
                _uiState.value = WordDetailUiState.NotFound
            }
        }.launchIn(viewModelScope)

    }

    fun toggleBookmark(state: Boolean) {
        wordItem?.let { wordItem ->
            val bookmarkWord = BookmarkWord(
                word = wordItem.word,
                mean = wordItem.mean
            )
            if (state) {
                addWordUseCase(bookmarkWord).launchIn(viewModelScope)
            } else {
                deleteWordUseCase(bookmarkWord).launchIn(viewModelScope)
            }
        }
    }
}
