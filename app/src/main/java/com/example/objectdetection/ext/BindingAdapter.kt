package com.example.objectdetection.ext

import android.animation.Animator
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("onLottieAnimateState")
fun LottieAnimationView.onLottieAnimateState(state : Function1<LottieAnimateState,Unit>?){
    this.addAnimatorListener(object :Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator) {
            state?.invoke(LottieAnimateState.Start)
        }

        override fun onAnimationEnd(animation: Animator) {
            state?.invoke(LottieAnimateState.End)
        }

        override fun onAnimationCancel(animation: Animator) {
            state?.invoke(LottieAnimateState.Cancel)
        }

        override fun onAnimationRepeat(animation: Animator) {
            state?.invoke(LottieAnimateState.Repeat)
        }

    })
}







sealed interface LottieAnimateState {
    object Start : LottieAnimateState
    object End : LottieAnimateState
    object Cancel : LottieAnimateState
    object Repeat : LottieAnimateState
}
