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
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner
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
                loginViewModel.login()
                true
            } else {
                false
            }
        }
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