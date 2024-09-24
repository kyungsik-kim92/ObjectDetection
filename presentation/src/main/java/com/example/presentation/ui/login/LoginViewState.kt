package com.example.presentation.ui.login

import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState


data class LoginViewState(
    val isEnable: Boolean = true,
    val isProgress: Boolean = false
) : ViewState

sealed interface LoginViewEvent : ViewEvent {
    data object RouteRegister : LoginViewEvent
    data class Error(val message: String) : LoginViewEvent
    data object RouteHome : LoginViewEvent
}