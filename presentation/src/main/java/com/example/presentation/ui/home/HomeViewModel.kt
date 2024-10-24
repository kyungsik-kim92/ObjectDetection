package com.example.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.firebase.DeleteWordUseCase
import com.example.model.BookmarkWord
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deleteWordUseCase: DeleteWordUseCase
) : BaseViewModel() {

    fun deleteBookmark(item: BookmarkWord) {
        deleteWordUseCase(item).onEach { isSuccess ->
            if (isSuccess) {
                onChangedViewEvent(HomeViewEvent.DeleteBookmark(item))
            }
        }.launchIn(viewModelScope)
    }
}