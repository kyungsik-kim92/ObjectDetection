package com.example.objectdetection.ui.search.detect

import android.app.Application
import androidx.databinding.ObservableField
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.ext.addWord
import com.example.objectdetection.ext.deleteWord
import com.example.objectdetection.ext.getWordList
import com.example.objectdetection.ext.ioScope
import com.example.objectdetection.ui.adapter.WordItem
import com.example.objectdetection.data.repo.SearchWordRepository
import com.example.objectdetection.network.response.DictionaryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.objectdetection.util.Result

@HiltViewModel
class SelectDetectionViewModel @Inject constructor(
    app: Application,
    private val searchWordRepository: SearchWordRepository,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel(app) {

    private val wordItemObservableField = ObservableField<WordItem>()

    fun searchMeanWord(word: String?) {
        word?.let {
            viewStateChanged(SelectDetectionViewState.ShowProgress)

            ioScope {
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
                                        viewStateChanged(
                                            SelectDetectionViewState.GetSearchWord(
                                                result.data[0]
                                            )
                                        )
                                    } else {
                                        viewStateChanged(SelectDetectionViewState.NotSearchWord)
                                        viewStateChanged(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                                    }
                                }

                                is Result.Error -> {
                                    viewStateChanged(SelectDetectionViewState.NotSearchWord)
                                    viewStateChanged(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                                }
                            }

                        } else {
                            viewStateChanged(SelectDetectionViewState.NotSearchWord)
                            viewStateChanged(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                        }

                    }

                    is Result.Error -> {
                        viewStateChanged(SelectDetectionViewState.NotSearchWord)
                        viewStateChanged(SelectDetectionViewState.ShowToast("단어를 찾을 수 없습니다."))
                    }

                }
            }

            viewStateChanged(SelectDetectionViewState.HideProgress)
        }
    }

    fun checkBookmark() {
        firebaseRepository.getWordList { bookmarkList ->
            if (bookmarkList != null) {
                val filterList =
                    bookmarkList.filter {
                        (it.word == wordItemObservableField.get()!!.word) && (it.mean == wordItemObservableField.get()!!.mean)
                    }
                viewStateChanged(SelectDetectionViewState.BookmarkState(filterList.isNotEmpty()))
            } else {
                viewStateChanged(SelectDetectionViewState.BookmarkState(false))
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
                        viewStateChanged(SelectDetectionViewState.ShowToast("즐겨찾기 추가를 실패하였습니다."))
                    }
                }
            }
        } else {
            ioScope {
                firebaseRepository.deleteWord(
                    wordItemObservableField.get()!!.toBookmarkWord()
                ) { isDeleteBookmark ->
                    if (!isDeleteBookmark) {
                        viewStateChanged(SelectDetectionViewState.ShowToast("즐겨찾기 제거를 실패하였습니다."))
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
