package com.example.presentation.ui.search.word

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.data.ext.addWord
import com.example.data.ext.deleteWord
import com.example.data.ext.getWordList
import com.example.domain.repo.FirebaseRepository
import com.example.domain.repo.SearchWordRepository
import com.example.model.WordItem
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.model.common.Result

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val searchWordRepository: com.example.domain.repo.SearchWordRepository,
    private val firebaseRepository: com.example.domain.repo.FirebaseRepository
) : BaseViewModel() {

    val wordItemObservableField = ObservableField<WordItem>()

    fun searchMeanWord() {

        onChangedViewState(WordDetailViewState.ShowProgress)

        wordItemObservableField.get()?.let { wordItem ->
            viewModelScope.launch(Dispatchers.IO) {
                when (val result = searchWordRepository.searchMeanWord(wordItem.word)) {
                    is Result.Success -> {

                        if (result.data.isNotEmpty()) {
                            onChangedViewState(WordDetailViewState.GetSearchWord(result.data[0]))
                        } else {
                            onChangedViewState(WordDetailViewState.NotSearchWord)
                            onChangedViewState(WordDetailViewState.ShowToast("단어를 찾을 수 없습니다."))
                        }
                    }

                    is Result.Error -> {
                        onChangedViewState(WordDetailViewState.NotSearchWord)
                        onChangedViewState(WordDetailViewState.ShowToast("단어를 찾을 수 없습니다."))
                    }
                }
            }
        }

        onChangedViewState(WordDetailViewState.HideProgress)
    }

    fun checkBookmark() {
        firebaseRepository.getWordList { bookmarkList ->

            if (bookmarkList != null) {
                val filterList =
                    bookmarkList.filter {
                        (it.word == wordItemObservableField.get()!!.word) &&
                                (it.mean == wordItemObservableField.get()!!.mean)
                    }
                onChangedViewState(WordDetailViewState.BookmarkState(filterList.isNotEmpty()))
            } else {
                onChangedViewState(WordDetailViewState.BookmarkState(false))
            }
        }
    }


    fun toggleBookmark(state: Boolean) {
        if (state) {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.addWord(
                    wordItemObservableField.get()!!.toBookmarkWord()
                ) { isAddBookmark ->
                    if (!isAddBookmark) {
                        onChangedViewState(WordDetailViewState.ShowToast("즐겨찾기 추가를 실패하였습니다."))
                    }
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.deleteWord(
                    wordItemObservableField.get()!!.toBookmarkWord()
                ) { isDeleteBookmark ->
                    if (!isDeleteBookmark) {
                        onChangedViewState(WordDetailViewState.ShowToast("즐겨찾기 제거를 실패하였습니다."))
                    }
                }
            }
        }
    }

}