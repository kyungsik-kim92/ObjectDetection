package com.example.presentation.ui.register

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.CheckInputRegisterUseCase
import com.example.domain.usecase.firebase.CheckRegisterState
import com.example.domain.usecase.firebase.FirebaseRegisterUseCase
import com.example.domain.usecase.firebase.RegisterErrorType
import com.example.presentation.base.BaseViewModel
import com.example.presentation.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val checkInputRegisterUseCase: CheckInputRegisterUseCase,
    private val firebaseRegisterUseCase: FirebaseRegisterUseCase
) : BaseViewModel() {


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
                    onChangedViewState(
                        RegisterViewState(
                            isEnable = false,
                            isLoading = true
                        )
                    )
                }.map { isSuccessful ->
                    if (isSuccessful) {
                        onChangedViewEvent(RegisterViewEvent.RouteHome)
                    } else {
                        onChangedViewEvent(ViewEvent.ShowToast("회원가입을 실패하였습니다."))
                        onChangedViewState(
                            RegisterViewState(
                                isEnable = true,
                                isLoading = false
                            )
                        )
                    }

                }.launchIn(viewModelScope)
            }
        }
    }

    private fun processRegisterError(type: RegisterErrorType) {
        viewModelScope.launch {
            when (type) {
                RegisterErrorType.NotInputEmail -> {
                    onChangedViewEvent(ViewEvent.ShowToast("이메일을 입력해주세요."))
                }

                RegisterErrorType.NotInputPassword -> {
                    onChangedViewEvent(ViewEvent.ShowToast("비밀번호를 입력해 주세요."))
                }

                RegisterErrorType.NotMatchPassword -> {
                    onChangedViewEvent(ViewEvent.ShowToast("비밀번호 재입력을 올바르게 입력해 주세요."))
                }
            }
        }
    }
}