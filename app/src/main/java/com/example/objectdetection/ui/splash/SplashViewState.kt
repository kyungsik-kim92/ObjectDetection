package com.example.objectdetection.ui.splash

import com.example.presentation.base.ViewState

sealed interface SplashViewState : ViewState {
    object RouteLogin : SplashViewState

}