package com.example.presentation.ui.login

import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState


sealed class LoginViewState : ViewState {
    object RouteHome : LoginViewState()
    data class Error(val message: String) : LoginViewState()
    data class EnableInput(val isEnable: Boolean) : LoginViewState()
    object ShowProgress : LoginViewState()
    object HideProgress : LoginViewState()
}

sealed interface LoginViewEvent : ViewEvent {

    object RouteRegister : LoginViewEvent


}