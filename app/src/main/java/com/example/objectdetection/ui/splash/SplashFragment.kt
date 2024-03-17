package com.example.objectdetection.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.base.ViewEvent
import com.example.objectdetection.base.ViewState
import com.example.objectdetection.databinding.FragmentSplashBinding
import com.example.objectdetection.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override val viewModel by viewModels<SplashViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initUi()
    }

    override fun initUi() {


//        binding.lottieView.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
//                findNavController().navigate(action)
////                startActivity(Intent(this@SplashActivity, LoginFragment::class.java).apply {
////                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////                })
//            }
//
//            override fun onAnimationCancel(animation: Animator) {
//            }
//
//            override fun onAnimationRepeat(animation: Animator) {
//            }
//
//        })
//    }
    }
    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is SplashViewState.RouteLogin ->
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {

    }


}