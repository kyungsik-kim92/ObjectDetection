package com.example.presentation.ui.search.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookmarkWord
import com.example.domain.model.WordItem
import com.example.domain.usecase.firebase.AddWordUseCase
import com.example.domain.usecase.firebase.DeleteWordUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.word.SearchWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
        val selectedWord = wordItem.word

        this.wordItem = wordItem
        _uiState.value = WordDetailUiState.Loading

        viewModelScope.launch {
            val searchResult = searchWordUseCase(wordItem.word).firstOrNull()?.items.orEmpty()
            if (searchResult.isEmpty()) {
                _uiState.value = WordDetailUiState.NotFound
                return@launch
            }
            val item = searchResult.first()

            getBookmarkWordListUseCase().collect { bookmarkList ->
                val isBookmark = bookmarkList.any { it.word == selectedWord }
                _uiState.value = WordDetailUiState.Success(item, isBookmark)
            }
        }
    }

    fun toggleBookmark(state: Boolean) {
        wordItem?.let { wordItem ->
            val bookmarkWord = BookmarkWord(
                word = wordItem.word,
                mean = wordItem.mean
            )
            viewModelScope.launch {
                if (state) {
                    addWordUseCase(bookmarkWord).first()
                } else {
                    deleteWordUseCase(bookmarkWord).first()
                }
            }
        }
    }
}
