package com.example.objectdetection.ui.splash

import com.example.presentation.base.BaseViewModel
import com.example.objectdetection.ext.LottieAnimateState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    val animateState: Function1<LottieAnimateState, Unit> = ::onAnimationState

    private fun onAnimationState(state: LottieAnimateState) {
        when (state) {
            LottieAnimateState.Start -> {}
            LottieAnimateState.End -> {
                onChangedViewState(SplashViewState.RouteLogin)

            }

            LottieAnimateState.Cancel -> {}
            LottieAnimateState.Repeat -> {}
        }
    }

}