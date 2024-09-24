package com.example.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.CheckInputLoginUseCase
import com.example.domain.usecase.firebase.CheckLoginState
import com.example.domain.usecase.firebase.FirebaseLoginUseCase
import com.example.domain.usecase.firebase.LoginErrorType
import com.example.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkInputLoginUseCase: CheckInputLoginUseCase,
    private val firebaseLoginUseCase: FirebaseLoginUseCase
) : BaseViewModel() {

    val inputEmailStateFlow: MutableStateFlow<String?> = MutableStateFlow("")
    val inputPasswordStateFlow: MutableStateFlow<String?> = MutableStateFlow("")


    fun login() {
        when (val result = checkInputLoginUseCase(
            inputEmailStateFlow.value.orEmpty(),
            inputPasswordStateFlow.value.orEmpty()
        )) {
            is CheckLoginState.Failure -> {
                processLoginError(result.type)
            }

            CheckLoginState.Success -> {
                firebaseLoginUseCase(
                    inputEmailStateFlow.value.orEmpty(),
                    inputPasswordStateFlow.value.orEmpty()
                ).onStart {
                    onChangedViewState(
                        LoginViewState(
                            isEnable = false,
                            isProgress = true
                        )
                    )
                }.map { isSuccessful ->
                    if (isSuccessful) {
                        onChangedViewEvent(LoginViewEvent.RouteHome)
                    } else {
                        onChangedViewEvent(LoginViewEvent.Error("로그인을 실패하였습니다."))
                        onChangedViewState(
                            LoginViewState(
                                isEnable = true,
                                isProgress = false
                            )
                        )
                    }

                }.launchIn(viewModelScope)
            }
        }
    }

    private fun processLoginError(type: LoginErrorType) {
        viewModelScope.launch {
            when (type) {
                LoginErrorType.NotInputEmail -> {
                    onChangedViewEvent(LoginViewEvent.Error("이메일을 입력해주세요."))
                }

                LoginErrorType.InvalidEmail -> {
                    onChangedViewEvent(LoginViewEvent.Error("이메일 형식이 올바르지 않습니다."))
                }

                LoginErrorType.NotInputPassword -> {
                    onChangedViewEvent(LoginViewEvent.Error("비밀번호를 입력해주세요."))
                }
            }
        }
    }


    fun register() {
        onChangedViewEvent(LoginViewEvent.RouteRegister)
    }


}