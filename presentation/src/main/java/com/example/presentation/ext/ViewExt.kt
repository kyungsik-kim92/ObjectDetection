package com.example.presentation.ext

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.routeLoginFragment() {
    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
}

fun Fragment.showToast(context: Context = this.requireContext(), message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

