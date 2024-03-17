package com.example.flowtimer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class MainViewModel : ViewModel() {
    companion object {
        private var INIT_TIME = 10
    }

    fun countDownTimerFlow() = flow {

        while (INIT_TIME > 0) {
            delay(1000)
            INIT_TIME--
            emit(INIT_TIME)
        }

    }

//    private val _stateFlow = MutableStateFlow(10)
//    val stateFlow = _stateFlow.asStateFlow()
//
//   suspend fun countDownTimer() {
//        while (INIT_TIME > 0) {
//            delay(1000)
//            INIT_TIME--
//            _stateFlow.value = INIT_TIME
//
//        }
//
//    }
}