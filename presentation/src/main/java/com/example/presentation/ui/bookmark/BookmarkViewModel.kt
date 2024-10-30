package com.example.presentation.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.GetWordListUseCase
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getWordListUseCase: GetWordListUseCase
) : BaseViewModel() {

    init {
        getWordListUseCase()
            .map { BookmarkViewState(it) }
            .onEach(::onChangedViewState)
            .launchIn(viewModelScope)
    }
}