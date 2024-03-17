package com.example.objectdetection.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.objectdetection.ext.uiScope
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
        scope = viewModelScope, // viewModelScope가 살아있을 때 까지 구독을 계속 한다
        started = SharingStarted.WhileSubscribed(5000),
        // 구독자가 없어진 후 5초 후에 동작을 멈춤
        initialValue = ViewState.Idle
        // 초기값 설정

        // 인터넷에서 stateIn은 Flow타입(콜드스트림)을 StateFlow(핫스트림)으로 변환하는 함수라고 하는데
        // _viewState 변수는 StateFlow타입인데 왜 stateIn을 쓴걸까요?
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