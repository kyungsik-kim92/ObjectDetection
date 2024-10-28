package com.example.presentation.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.firebase.GetWordListUseCase
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val getWordListUseCase: GetWordListUseCase
) : BaseViewModel() {

    fun getBookmarkList() {
        getWordListUseCase().onEach { bookmarkList ->
            onChangedViewState(BookmarkViewState(bookmarkList))
        }.launchIn(viewModelScope)
    }
}