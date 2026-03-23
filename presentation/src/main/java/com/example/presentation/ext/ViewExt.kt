package com.example.presentation.ext

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.domain.model.WordItem
import com.example.presentation.R
import com.example.presentation.ui.home.HomeFragmentDirections
import com.example.presentation.ui.search.detect.CameraFragmentDirections
import com.example.presentation.ui.search.word.WordContentFragmentDirections
import com.example.presentation.ui.splash.SplashFragmentDirections

fun Fragment.routeLoginFragment() {
    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
}

fun Fragment.routePermission() {
    Navigation.findNavController(requireActivity(), R.id.fragment_container)
        .navigate(CameraFragmentDirections.actionCameraToPermissions())
}

fun Fragment.routeWordDetail(item: WordItem) {
    val action = WordContentFragmentDirections.actionContentToDetail(item)
    findNavController().navigate(action)
}

fun Fragment.routeSelectItem(item: String) {
    val action = CameraFragmentDirections.actionCameraToSelectDetectItem(item)
    findNavController().navigate(action)
}

fun Fragment.routeWordDetailFromMain(word: String, mean: String) {
    val wordItem = WordItem(word = word, mean = mean)
    val action = HomeFragmentDirections.actionHomeFragmentToWordDetail(wordItem)
    findNavController().navigate(action)
}

