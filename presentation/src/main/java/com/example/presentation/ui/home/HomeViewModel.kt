package com.example.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.DeleteWordUseCase
import com.example.model.BookmarkWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deleteWordUseCase: DeleteWordUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent: SharedFlow<HomeUiEvent> = _uiEvent.asSharedFlow()

    fun deleteBookmark(item: BookmarkWord) {
        deleteWordUseCase(item)
            .onEach { isSuccess ->
                if (isSuccess) {
                    viewModelScope.launch {
                        _uiEvent.emit(HomeUiEvent.DeleteBookmark(item))
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}