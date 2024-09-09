package com.example.presentation.ui.register

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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


    override fun initUi() {
        binding.inputPassRegisterOk.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.register()
                true
            } else {
                false
            }
        }
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
//                val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
//                findNavController().navigate(action)
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