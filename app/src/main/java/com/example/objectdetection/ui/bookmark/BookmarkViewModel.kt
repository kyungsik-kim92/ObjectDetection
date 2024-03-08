package com.example.objectdetection.ui.bookmark

import android.app.Application
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.ext.getWordList
import com.example.objectdetection.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    app : Application,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel(app) {

    fun getBookmarkList() {

        ioScope {
            firebaseRepository.getWordList {list->
                if (!list.isNullOrEmpty()) {
                    viewStateChanged(BookmarkViewState.GetBookmarkList(list))
                } else {
                    viewStateChanged(BookmarkViewState.EmptyBookmarkList)
                }
            }
        }
    }
}