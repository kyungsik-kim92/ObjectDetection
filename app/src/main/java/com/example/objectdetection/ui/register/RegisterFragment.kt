package com.example.objectdetection.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.base.ViewEvent
import com.example.objectdetection.base.ViewState
import com.example.objectdetection.databinding.FragmentRegisterBinding
import com.example.objectdetection.ext.showToast
import com.example.objectdetection.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    override val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    override fun initUi() {
        binding.inputPassRegisterOk.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.register()
                true
            } else {
                false
            }
        }
//        viewModel.viewState.map(::onChangedViewState).launchIn(lifecycleScope)
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {

            is RegisterViewState.Error -> {
                showToast(message = state.message)
            }


            is RegisterViewState.EnableInput -> {
                with(binding) {
                    inputEmailRegister.isEnabled = state.isEnable
                    inputPassRegisterOk.isEnabled = state.isEnable
                    inputPassRegister.isEnabled = state.isEnable
                }
            }


            is RegisterViewState.RouteHome -> {
                val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                findNavController().navigate(action)
            }


            is RegisterViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }


            is RegisterViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {}




}