package com.example.flowtimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var initTime = 10


    private val _countdownState = MutableStateFlow(initTime)
    val countdownState: StateFlow<Int> = _countdownState.asStateFlow()

    init {
        countdown()
    }

    private fun countdown() {
        viewModelScope.launch {
            while (initTime >= 0) {
                _countdownState.emit(initTime)
                delay(1000)
                initTime--
            }
        }
    }

//    fun countDownTimerFlow() = flow {
//
//        while (INIT_TIME > 0) {
//            delay(1000)
//            INIT_TIME--
//            emit(INIT_TIME)
//        }
//
//    }


}