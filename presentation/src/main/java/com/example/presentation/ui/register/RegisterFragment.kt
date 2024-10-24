package com.example.presentation.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentRegisterBinding
import com.example.presentation.ext.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    override val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    override fun initUi() {}

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is RegisterViewState -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = state.isLoading
                with(binding) {
                    inputEmailRegister.isEnabled = state.isEnable
                    inputPassRegisterOk.isEnabled = state.isEnable
                    inputPassRegister.isEnabled = state.isEnable
                }
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is RegisterViewEvent.Error -> {
                showToast(message = event.message)
            }

            is RegisterViewEvent.RouteHome -> {
                val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                findNavController().navigate(action)
            }

        }
    }


}