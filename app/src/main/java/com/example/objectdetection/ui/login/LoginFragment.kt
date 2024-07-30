package com.example.objectdetection.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.base.ViewEvent
import com.example.objectdetection.base.ViewState
import com.example.objectdetection.databinding.FragmentLoginBinding
import com.example.objectdetection.ext.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override val viewModel by viewModels<LoginViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    override fun initUi() {
        binding.inputPassLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.login()
                true
            } else {
                false
            }
        }
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {


            is LoginViewState.Error -> {
                showToast(message = state.message)
            }


            is LoginViewState.EnableInput -> {
                with(binding) {
                    inputEmailLogin.isEnabled = state.isEnable
                    inputPassLogin.isEnabled = state.isEnable
                }
            }


            is LoginViewState.RouteHome -> {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }


            is LoginViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }


            is LoginViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is LoginViewEvent.RouteRegister -> {
                with(binding) {
                    inputEmailLogin.text.clear()
                    inputPassLogin.text.clear()
                }
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
                // 복습으로 뒤로가기가 왜 안되었었는지 이해한 내용
                // RouteRegister를 State로 처리하면 뒤로가기 했을 때 viewState 값이 계속 작용되어
                // register 창으로 반복 이동 되는 것을 Event처리하여 한 번만 값을 전달하게 바꿈.
            }
        }
    }


}