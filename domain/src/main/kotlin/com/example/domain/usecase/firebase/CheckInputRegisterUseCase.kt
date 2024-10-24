package com.example.domain.usecase.firebase

import javax.inject.Inject

class CheckInputRegisterUseCase @Inject constructor() {
    operator fun invoke(email: String, password: String, passwordOk: String): CheckRegisterState {
        if (email.isEmpty()) {
            return CheckRegisterState.Failure(RegisterErrorType.NotInputEmail)
        }
        if (password.isEmpty()) {
            return CheckRegisterState.Failure(RegisterErrorType.NotInputPassword)
        }
        if (password != passwordOk) {
            return CheckRegisterState.Failure(RegisterErrorType.NotMatchPassword)
        }
        return CheckRegisterState.Success
    }
}


sealed interface CheckRegisterState {
    data class Failure(val type: RegisterErrorType) : CheckRegisterState
    data object Success : CheckRegisterState
}

sealed interface RegisterErrorType {
    data object NotInputEmail : RegisterErrorType
    data object NotInputPassword : RegisterErrorType
    data object NotMatchPassword : RegisterErrorType

}