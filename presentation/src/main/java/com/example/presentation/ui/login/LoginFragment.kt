package com.example.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiState()
        observeEvents()
    }


    private fun initUi() {
        binding.inputPassLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.root.post { // 마지막 텍스트를 인식못해서 완료 버튼으로 로그인 안되는 현상 수정
                    loginViewModel.login(
                        binding.inputEmailLogin.text.toString().trim(),
                        binding.inputPassLogin.text.toString()
                    )
                }
                true
            } else false
        }
        binding.btnLogin.setOnClickListener {
            loginViewModel.login(
                binding.inputEmailLogin.text.toString(),
                binding.inputPassLogin.text.toString()
            )
        }
        binding.btnRegister.setOnClickListener { loginViewModel.register() }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Idle -> {
                            binding.progressbar.isVisible = false
                            setInputsEnabled(true)
                        }

                        is LoginUiState.Loading -> {
                            binding.progressbar.bringToFront()
                            binding.progressbar.isVisible = true
                            setInputsEnabled(false)
                        }

                        is LoginUiState.Success -> {
                            binding.progressbar.isVisible = false
                            setInputsEnabled(true)
                        }
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiEvent.collect { event ->
                    when (event) {
                        is LoginUiEvent.RouteRegister -> {
                            clearInputs()
                            val action =
                                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                            findNavController().navigate(action)
                        }

                        is LoginUiEvent.RouteHome -> {
                            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                            findNavController().navigate(action)
                        }

                        is LoginUiEvent.ShowToast -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun setInputsEnabled(enabled: Boolean) {
        with(binding) {
            inputEmailLogin.isEnabled = enabled
            inputPassLogin.isEnabled = enabled
            btnLogin.isEnabled = enabled
            btnRegister.isEnabled = enabled
        }
    }

    private fun clearInputs() {
        with(binding) {
            inputEmailLogin.text.clear()
            inputPassLogin.text.clear()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}