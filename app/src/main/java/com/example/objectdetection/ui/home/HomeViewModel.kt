package com.example.objectdetection.ui.home

import android.app.Application
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.ext.addWord
import com.example.objectdetection.ext.deleteWord
import com.example.objectdetection.ext.ioScope
import com.example.objectdetection.data.model.BookmarkWord
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel(app) {

    fun addBookmark(item: BookmarkWord) {
        ioScope {
            firebaseRepository.addWord(item) {isSuccess ->
                if(isSuccess) {
                    viewStateChanged(HomeViewState.AddBookmark(item))
                } else {

                }
            }
        }
    }

    fun deleteBookmark(item: BookmarkWord) {

        ioScope {
            firebaseRepository.deleteWord(item) {isSuccess ->
                if(isSuccess) {
                    viewStateChanged(HomeViewState.DeleteBookmark(item))
                } else {

                }
            }
        }
    }

}