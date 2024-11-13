package com.example.presentation.ext

import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.model.WordItem
import com.example.presentation.R
import com.example.presentation.ui.search.word.ARG_WORD
import com.example.presentation.ui.splash.SplashFragmentDirections

fun Fragment.routeLoginFragment() {
    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
}

fun Fragment.routeWordDetail(item: WordItem) {
    findNavController().navigate(
        R.id.action_content_to_detail,
        bundleOf(ARG_WORD to item)
    )
}

fun Fragment.showToast(context: Context = this.requireContext(), message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

