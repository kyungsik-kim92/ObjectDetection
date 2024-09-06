package com.example.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Idle)
    val viewState: StateFlow<ViewState> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ViewState.Idle
    )

    private val _viewEvent = MutableSharedFlow<ViewEvent>()
    val viewEvent: Flow<ViewEvent> = _viewEvent

    protected fun onChangedViewState(state: ViewState) = viewModelScope.launch {
        _viewState.value = state
    }

    protected fun onChangedViewEvent(event: ViewEvent) = viewModelScope.launch {
        _viewEvent.emit(event)
    }
}

interface ViewState {
    object Idle : ViewState
}

interface ViewEvent {
    data class ShowToast(val message: String) : ViewEvent
}