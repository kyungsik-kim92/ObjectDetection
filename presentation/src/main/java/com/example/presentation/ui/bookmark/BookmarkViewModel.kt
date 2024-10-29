package com.example.presentation.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.firebase.GetCurrentFirebaseUserUseCase
import com.example.domain.usecase.firebase.GetWordListUseCase
import com.example.model.BookmarkWord
import com.example.presentation.base.BaseViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getWordListUseCase: GetWordListUseCase
) : BaseViewModel() {

    init {
        getWordListUseCase().onEach { bookmarkList ->
            onChangedViewState(BookmarkViewState(bookmarkList))
        }.launchIn(viewModelScope)
    }
}