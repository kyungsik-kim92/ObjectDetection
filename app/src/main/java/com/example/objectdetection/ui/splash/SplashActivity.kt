package com.example.objectdetection.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseActivity
import com.example.objectdetection.databinding.ActivitySplashBinding
import com.example.objectdetection.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.lottieView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
               startActivity(Intent(this@SplashActivity, LoginActivity::class.java).apply {
                   addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
               })
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        })
    }

}