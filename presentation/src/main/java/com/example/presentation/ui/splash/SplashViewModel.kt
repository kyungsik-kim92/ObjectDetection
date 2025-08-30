package com.example.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.word.UpdateWordListUseCase
import com.example.presentation.ext.LottieAnimateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.plus
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val updateWordListUseCase: UpdateWordListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    val animateState: Function1<LottieAnimateState, Unit> = ::onAnimationState

    private fun onAnimationState(state: LottieAnimateState) {
        when (state) {
            LottieAnimateState.Start -> {
                updateWordListUseCase()
                    .onStart { _uiState.value = SplashUiState.Loading }
                    .timeout(10.seconds)
                    .onEach {
                        _uiState.value = SplashUiState.RouteLogin
                    }
                    .catch {
                        _uiState.value = SplashUiState.RouteLogin
                    }
                    .launchIn(viewModelScope.plus(Dispatchers.IO))

            }

            LottieAnimateState.End -> {
                _uiState.value = SplashUiState.RouteLogin
            }

            LottieAnimateState.Cancel -> {}
            LottieAnimateState.Repeat -> {}
        }
    }

}