package com.example.presentation.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.example.presentation.base.BaseViewModel
import com.example.data.ext.getWordList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val firebaseRepository: com.example.data.repo.FirebaseRepository
) : BaseViewModel() {

    fun getBookmarkList() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getWordList {list->
                if (!list.isNullOrEmpty()) {
                    onChangedViewState(BookmarkViewState.GetBookmarkList(list))
                } else {
                    onChangedViewState(BookmarkViewState.EmptyBookmarkList)
                }
            }
        }
    }
}