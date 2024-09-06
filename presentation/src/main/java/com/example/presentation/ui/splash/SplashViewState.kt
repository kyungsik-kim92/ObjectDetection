package com.example.presentation.ui.splash

import com.example.presentation.base.ViewState

sealed interface SplashViewState : ViewState {
    object RouteLogin : SplashViewState

}