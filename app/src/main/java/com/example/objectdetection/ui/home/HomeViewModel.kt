package com.example.objectdetection.ui.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.ext.addWord
import com.example.objectdetection.ext.deleteWord
import com.example.objectdetection.ext.ioScope
import com.example.objectdetection.data.model.BookmarkWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

//    fun addBookmark(item: BookmarkWord) {
//        viewModelScope.launch(Dispatchers.IO) {
//            firebaseRepository.addWord(item) { isSuccess ->
//                if (isSuccess) {
//                    onChangedViewEvent(HomeViewEvent.AddBookmark(item))
//                } else {
//
//                }
//            }
//        }
//    }

    fun deleteBookmark(item: BookmarkWord) {

        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.deleteWord(item) { isSuccess ->
                if (isSuccess) {
                    onChangedViewEvent(HomeViewEvent.DeleteBookmark(item))
                } else {

                }
            }
        }
    }

}