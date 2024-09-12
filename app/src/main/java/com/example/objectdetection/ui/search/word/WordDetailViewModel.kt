package com.example.objectdetection.ui.search.word

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.presentation.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.data.repo.SearchWordRepository
import com.example.objectdetection.ext.*
import com.example.objectdetection.ui.adapter.WordItem
import com.example.objectdetection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val searchWordRepository: SearchWordRepository,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    val wordItemObservableField = ObservableField<WordItem>()
    // ObservableField의 사용 이유를 검색해보고, LiveData와의 차이는 수명주기를 아느냐 모르느냐 인 것 같음.
    // LiveData를 쓰지않고 ObservableField를 사용한 이유가 궁금합니다.

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

            if(bookmarkList!= null){
                val filterList =
                    bookmarkList.filter {
                        (it.word == wordItemObservableField.get()!!.word) &&
                                (it.mean == wordItemObservableField.get()!!.mean)
                    }
                onChangedViewState(WordDetailViewState.BookmarkState(filterList.isNotEmpty()))
            }else{
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