package com.example.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.CheckInputRegisterUseCase
import com.example.domain.usecase.firebase.CheckRegisterState
import com.example.domain.usecase.firebase.FirebaseRegisterUseCase
import com.example.domain.usecase.firebase.RegisterErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val checkInputRegisterUseCase: CheckInputRegisterUseCase,
    private val firebaseRegisterUseCase: FirebaseRegisterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent: SharedFlow<RegisterUiEvent> = _uiEvent.asSharedFlow()

    val inputEmailStateFlow: MutableStateFlow<String?> = MutableStateFlow("")
    val inputPasswordStateFlow: MutableStateFlow<String?> = MutableStateFlow("")
    val inputPasswordOkStateFlow: MutableStateFlow<String?> = MutableStateFlow("")

    fun register() {
        when (val result = checkInputRegisterUseCase(
            inputEmailStateFlow.value.orEmpty(),
            inputPasswordStateFlow.value.orEmpty(),
            inputPasswordOkStateFlow.value.orEmpty()
        )) {
            is CheckRegisterState.Failure -> {
                processRegisterError(result.type)
            }

            CheckRegisterState.Success -> {
                firebaseRegisterUseCase(
                    inputEmailStateFlow.value.orEmpty(),
                    inputPasswordStateFlow.value.orEmpty()
                ).onStart {
                    _uiState.value = RegisterUiState.Loading
                }.map { isSuccessful ->
                    if (isSuccessful) {
                        _uiState.value = RegisterUiState.Success
                        viewModelScope.launch {
                            _uiEvent.emit(RegisterUiEvent.RouteHome)
                        }
                    } else {
                        _uiState.value = RegisterUiState.Idle
                        viewModelScope.launch {
                            _uiEvent.emit(RegisterUiEvent.ShowToast("회원가입을 실패하였습니다."))
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun processRegisterError(type: RegisterErrorType) {
        viewModelScope.launch {
            val message = when (type) {
                RegisterErrorType.NotInputEmail -> "이메일을 입력해주세요."
                RegisterErrorType.NotInputPassword -> "비밀번호를 입력해 주세요."
                RegisterErrorType.NotMatchPassword -> "비밀번호 재입력을 올바르게 입력해 주세요."
            }
            _uiEvent.emit(RegisterUiEvent.ShowToast(message))
        }
    }
}