package com.example.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentSplashBinding
import com.example.presentation.ext.routeLoginFragment
import com.example.presentation.ui.mypage.MyPageFragment.Companion.KEY_LOGOUT
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity().intent.getBooleanExtra(KEY_LOGOUT, false)) {
            routeLoginFragment()
        }
    }

    override fun initUi() {}

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is SplashViewState.RouteLogin -> routeLoginFragment()
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {}


}