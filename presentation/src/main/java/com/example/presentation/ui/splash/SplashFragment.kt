package com.example.presentation.ui.splash

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.presentation.databinding.FragmentSplashBinding
import com.example.presentation.ext.routeLoginFragment
import com.example.presentation.ui.dialog.WithdrawDialog.Companion.KEY_LOGIN_DIRECT
import com.example.presentation.ui.mypage.MyPageFragment.Companion.KEY_LOGOUT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        checkLogoutIntent()
        observeUiState()
    }

    private fun initUi() {
        binding.lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                splashViewModel.animateState(LottieAnimateState.Start)
            }

            override fun onAnimationEnd(animation: Animator) {
                splashViewModel.animateState(LottieAnimateState.End)
            }

            override fun onAnimationCancel(animation: Animator) {
                splashViewModel.animateState(LottieAnimateState.Cancel)
            }

            override fun onAnimationRepeat(animation: Animator) {
                splashViewModel.animateState(LottieAnimateState.Repeat)
            }
        })
    }

    private fun checkLogoutIntent() {
        when {
            requireActivity().intent.getBooleanExtra(KEY_LOGOUT, false) -> {
                routeLoginFragment()
            }

            requireActivity().intent.getBooleanExtra(KEY_LOGIN_DIRECT, false) -> {
                routeLoginFragment()
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.uiState.collect { state ->
                    when (state) {
                        is SplashUiState.Loading -> {}

                        is SplashUiState.RouteLogin -> {
                            routeLoginFragment()
                        }
                    }
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}