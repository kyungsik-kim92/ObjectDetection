package com.example.objectdetection.ui.splash

import com.example.objectdetection.base.ViewState

sealed interface SplashViewState : ViewState {
    object RouteLogin : SplashViewState

}