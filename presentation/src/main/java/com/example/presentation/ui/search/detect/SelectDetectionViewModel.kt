package com.example.presentation.ui.search.detect

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.ext.addWord
import com.example.domain.ext.deleteWord
import com.example.domain.ext.getWordList
import com.example.domain.repo.FirebaseRepository
import com.example.domain.repo.SearchWordRepository
import com.example.model.WordItem
import com.example.model.api.DictionaryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectDetectionViewModel @Inject constructor(
    private val searchWordRepository: SearchWordRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SelectDetectionUiState>(SelectDetectionUiState.Loading)
    val uiState: StateFlow<SelectDetectionUiState> = _uiState.asStateFlow()

    private val wordItemObservableField = ObservableField<WordItem>()
    private var currentWord: DictionaryResponseItem? = null

    fun searchMeanWord(word: String?) {
        word?.let {
            _uiState.value = SelectDetectionUiState.Loading

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val dictionaryResponse = searchWordRepository.searchMeanWord(word)
                    val getData = dictionaryResponse.filter { it.word == word }

                    if (getData.isNotEmpty()) {
                        currentWord = getData[0]
                        wordItemObservableField.set(
                            WordItem(
                                getData[0].word,
                                getData[0].toMean(),
                            )
                        )
                        _uiState.value = SelectDetectionUiState.Success(getData[0])
                    } else {
                        _uiState.value = SelectDetectionUiState.NotFound
                    }
                } catch (e: Exception) {
                    _uiState.value = SelectDetectionUiState.Error("단어를 찾을 수 없습니다.")
                }
            }
        }
    }



fun checkBookmark() {
    firebaseRepository.getWordList { bookmarkList ->
        if (bookmarkList != null && wordItemObservableField.get() != null) {
            val filterList = bookmarkList.filter {
                (it.word == wordItemObservableField.get()!!.word) &&
                        (it.mean == wordItemObservableField.get()!!.mean)
            }
            _uiState.value = SelectDetectionUiState.BookmarkUpdated(filterList.isNotEmpty())
        } else {
            _uiState.value = SelectDetectionUiState.BookmarkUpdated(false)
        }
    }
}

fun toggleBookmark(state: Boolean) {
    wordItemObservableField.get()?.let { wordItem ->
        if (state) {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.addWord(wordItem.toBookmarkWord()) { isAddBookmark ->
                    if (!isAddBookmark) {
                        _uiState.value = SelectDetectionUiState.Error("즐겨찾기 추가를 실패하였습니다.")
                    }
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.deleteWord(wordItem.toBookmarkWord()) { isDeleteBookmark ->
                    if (!isDeleteBookmark) {
                        _uiState.value = SelectDetectionUiState.Error("즐겨찾기 제거를 실패하였습니다.")
                    }
                }
            }
        }
    }
}
}

fun DictionaryResponseItem.toMean(): String =
    if (meanings.isNotEmpty()) {
        meanings.first().definitions.first().definition
    } else {
        "-"
    }