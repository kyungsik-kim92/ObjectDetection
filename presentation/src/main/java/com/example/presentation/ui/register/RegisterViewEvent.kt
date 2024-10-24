package com.example.presentation.ui.register

import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState

data class RegisterViewState(
    val isEnable: Boolean = true,
    val isLoading: Boolean = false
) : ViewState


sealed interface RegisterViewEvent : ViewEvent {
    data object RouteHome : RegisterViewEvent
}