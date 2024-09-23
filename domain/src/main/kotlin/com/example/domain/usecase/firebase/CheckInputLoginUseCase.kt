package com.example.domain.usecase.firebase

import javax.inject.Inject

class CheckInputLoginUseCase @Inject constructor() {
    operator fun invoke(email: String, password: String): CheckLoginState {
        if (email.isEmpty()) {
            return CheckLoginState.Failure(LoginErrorType.NotInputEmail)
        }
        if (password.isEmpty()) {
            return CheckLoginState.Failure(LoginErrorType.NotInputPassword)
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return CheckLoginState.Failure(LoginErrorType.InvalidEmail)
        }
        return CheckLoginState.Success
    }
}

sealed

interface CheckLoginState {
    data class Failure(val type: LoginErrorType) : CheckLoginState
    data object Success : CheckLoginState

}

sealed interface LoginErrorType {
    data object NotInputEmail : LoginErrorType
    data object InvalidEmail : LoginErrorType
    data object NotInputPassword : LoginErrorType

}