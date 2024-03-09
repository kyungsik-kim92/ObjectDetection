package com.example.objectdetection.ui.search.word

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.model.BookmarkWord
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.data.repo.SearchWordRepository
import com.example.objectdetection.ext.*
import com.example.objectdetection.ui.adapter.WordItem
import com.example.objectdetection.util.Result
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val searchWordRepository: SearchWordRepository,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    val wordItemObservableField = ObservableField<WordItem>()

    fun searchMeanWord() {

        onChangedViewState(WordDetailViewState.ShowProgress)

        wordItemObservableField.get()?.let { wordItem ->
            ioScope {
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
            ioScope {
                firebaseRepository.addWord(
                    wordItemObservableField.get()!!.toBookmarkWord()
                ) { isAddBookmark ->
                    if (!isAddBookmark) {
                        onChangedViewState(WordDetailViewState.ShowToast("즐겨찾기 추가를 실패하였습니다."))
                    }
                }
            }
        } else {
            ioScope {
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