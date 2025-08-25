package com.example.presentation.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        observeEvents()
    }


    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.uiState.collect { state ->
                    when (state) {
                        is RegisterUiState.Idle -> {
                            binding.progressbar.isVisible = false
                            setInputsEnabled(true)
                        }

                        is RegisterUiState.Loading -> {
                            binding.progressbar.bringToFront()
                            binding.progressbar.isVisible = true
                            setInputsEnabled(false)
                        }

                        is RegisterUiState.Success -> {
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
                registerViewModel.uiEvent.collect { event ->
                    when (event) {
                        is RegisterUiEvent.RouteHome -> {
                            val action =
                                RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                            findNavController().navigate(action)
                        }

                        is RegisterUiEvent.ShowToast -> {
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
            inputEmailRegister.isEnabled = enabled
            inputPassRegisterOk.isEnabled = enabled
            inputPassRegister.isEnabled = enabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}