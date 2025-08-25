package com.example.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.CheckInputLoginUseCase
import com.example.domain.usecase.firebase.CheckLoginState
import com.example.domain.usecase.firebase.FirebaseLoginUseCase
import com.example.domain.usecase.firebase.LoginErrorType
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
class LoginViewModel @Inject constructor(
    private val checkInputLoginUseCase: CheckInputLoginUseCase,
    private val firebaseLoginUseCase: FirebaseLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent.asSharedFlow()

    val inputEmailStateFlow: MutableStateFlow<String?> = MutableStateFlow("")
    val inputPasswordStateFlow: MutableStateFlow<String?> = MutableStateFlow("")

    fun login() {
        when (val result = checkInputLoginUseCase(
            inputEmailStateFlow.value.orEmpty(), inputPasswordStateFlow.value.orEmpty()
        )) {
            is CheckLoginState.Failure -> {
                processLoginError(result.type)
            }

            CheckLoginState.Success -> {
                firebaseLoginUseCase(
                    inputEmailStateFlow.value.orEmpty(), inputPasswordStateFlow.value.orEmpty()
                ).onStart {
                    _uiState.value = LoginUiState.Loading
                }.map { isSuccessful ->
                    if (isSuccessful) {
                        _uiState.value = LoginUiState.Success
                        viewModelScope.launch {
                            _uiEvent.emit(LoginUiEvent.RouteHome)
                        }
                    } else {
                        _uiState.value = LoginUiState.Idle
                        viewModelScope.launch {
                            _uiEvent.emit(LoginUiEvent.ShowToast("로그인을 실패하였습니다."))
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }


    private fun processLoginError(type: LoginErrorType) {
        viewModelScope.launch {
            val message = when (type) {
                LoginErrorType.NotInputEmail -> "이메일을 입력해주세요."
                LoginErrorType.InvalidEmail -> "이메일 형식이 올바르지 않습니다."
                LoginErrorType.NotInputPassword -> "비밀번호를 입력해주세요."
            }
            _uiEvent.emit(LoginUiEvent.ShowToast(message))
        }
    }

    fun register() {
        viewModelScope.launch {
            _uiEvent.emit(LoginUiEvent.RouteRegister)
        }
    }
}