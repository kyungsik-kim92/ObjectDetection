package com.example.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookmarkWord
import com.example.domain.repo.FirebaseRepository
import com.example.objectdetection.ext.deleteWord
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

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