package com.example.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.word.UpdateWordListUseCase
import com.example.presentation.base.BaseViewModel
import com.example.presentation.ext.LottieAnimateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val updateWordListUseCase: UpdateWordListUseCase
) :
    BaseViewModel() {


    val animateState: Function1<LottieAnimateState, Unit> = ::onAnimationState

    private fun onAnimationState(state: LottieAnimateState) {
        when (state) {
            LottieAnimateState.Start -> {
                updateWordListUseCase().launchIn(viewModelScope.plus(Dispatchers.IO))

            }

            LottieAnimateState.End -> {
                onChangedViewState(SplashViewState.RouteLogin)

            }

            LottieAnimateState.Cancel -> {}
            LottieAnimateState.Repeat -> {}
        }
    }

}