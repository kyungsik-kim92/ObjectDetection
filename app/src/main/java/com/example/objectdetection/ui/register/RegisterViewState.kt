package com.example.objectdetection.ui.register

import com.example.presentation.base.ViewState

sealed class RegisterViewState : ViewState {

    object RouteHome : RegisterViewState()
    data class Error(val message: String) : RegisterViewState()
    data class EnableInput(val isEnable: Boolean) : RegisterViewState()
    object ShowProgress : RegisterViewState()
    object HideProgress : RegisterViewState()
}