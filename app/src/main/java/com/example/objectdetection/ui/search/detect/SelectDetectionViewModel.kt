package com.example.objectdetection.ui.search.detect

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.presentation.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.data.repo.SearchWordRepository
import com.example.objectdetection.ext.addWord
import com.example.objectdetection.ext.deleteWord
import com.example.objectdetection.ext.getWordList
import com.example.objectdetection.network.response.DictionaryResponseItem
import com.example.objectdetection.ui.adapter.WordItem
import com.example.objectdetection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectDetectionViewModel @Inject constructor(
    private val searchWordRepository: SearchWordRepository,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    private val wordItemObservableField = ObservableField<WordItem>()

    fun searchMeanWord(word: String?) {
        word?.let {
            onChangedViewState(SelectDetectionViewState.ShowProgress)

            viewModelScope.launch(Dispatchers.IO) {
                when (val result = searchWordRepository.searchMeanWord(word)) {

                    is Result.Success -> {

                        val getData = result.data.filter { it.word == word }

                        if (getData.isNotEmpty()) {
                            when (val result =
                                searchWordRepository.searchMeanWord(getData[0].word)) {
                                is Result.Success -> {
                                    if (result.data.isNotEmpty()) {
                                        result.data[0].word


                                        wordItemObservableField.set(
                                            WordItem(
                                                result.data[0].word,
                                                result.data[0].toMean(),
                                            )
                                        )
                                        onChangedViewState(
                                            SelectDetectionViewState.GetSearchWord(
                                                result.data[0]
                                            )
                                        )
                                    } else {
                                        onChangedViewState(SelectDetectionViewState.NotSearchWord)
                                        onChangedViewState(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                                    }
                                }

                                is Result.Error -> {
                                    onChangedViewState(SelectDetectionViewState.NotSearchWord)
                                    onChangedViewState(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                                }
                            }

                        } else {
                            onChangedViewState(SelectDetectionViewState.NotSearchWord)
                            onChangedViewState(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                        }

                    }

                    is Result.Error -> {
                        onChangedViewState(SelectDetectionViewState.NotSearchWord)
                        onChangedViewState(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                    }

                }
            }

            onChangedViewState(SelectDetectionViewState.HideProgress)
        }
    }

    fun checkBookmark() {
        firebaseRepository.getWordList { bookmarkList ->
            if (bookmarkList != null) {
                val filterList =
                    bookmarkList.filter {
                        (it.word == wordItemObservableField.get()!!.word) && (it.mean == wordItemObservableField.get()!!.mean)
                    }
                onChangedViewState(SelectDetectionViewState.BookmarkState(filterList.isNotEmpty()))
            } else {
                onChangedViewState(SelectDetectionViewState.BookmarkState(false))
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
                        onChangedViewState(SelectDetectionViewState.ShowToast("즐겨찾기 추가를 실패하였습니다."))
                    }
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.deleteWord(
                    wordItemObservableField.get()!!.toBookmarkWord()
                ) { isDeleteBookmark ->
                    if (!isDeleteBookmark) {
                        onChangedViewState(SelectDetectionViewState.ShowToast("즐겨찾기 제거를 실패하였습니다."))
                    }
                }
            }
        }
    }

}

//viewState.word.meanings.forEach { meaning ->
//
//                    when (meaning.partOfSpeech) {
//
//                        "noun" -> {
//                            binding.viewWordDetail.containerNoun.isVisible = true
//                            binding.viewWordDetail.noun.text = meaning.definitions[0].definition
//                        }
//
//                        "verb" -> {
//                            binding.viewWordDetail.containerVerb.isVisible = true
//                            binding.viewWordDetail.verb.text = meaning.definitions[0].definition
//                        }
//
//                        "adjective" -> {
//                            binding.viewWordDetail.containerAdjective.isVisible = true
//                            binding.viewWordDetail.adjective.text =
//                                meaning.definitions[0].definition
//                        }
//
//                        "adverb" -> {
//                            binding.viewWordDetail.containerAdverb.isVisible = true
//                            binding.viewWordDetail.adverb.text = meaning.definitions[0].definition
//                        }
//
//                        else -> {}
//                    }
//                }

fun DictionaryResponseItem.toMean(): String =
    if (meanings.isNotEmpty()) {
        meanings.first().definitions.first().definition
    } else {
        "-"
    }
