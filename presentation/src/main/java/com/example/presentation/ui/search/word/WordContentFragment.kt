package com.example.presentation.ui.search.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.presentation.databinding.FragmentWordContentBinding
import com.example.presentation.ext.routeWordDetail
import com.example.presentation.ui.adapter.WordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordContentFragment : Fragment() {

    private var _binding: FragmentWordContentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WordContentViewModel>()
    private val wordAdapter = WordAdapter(::routeWordDetail)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiState()
    }

    private fun initUi() {
        binding.rvWord.adapter = wordAdapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is WordContentUiState.Loading -> {
                            binding.rvWord.isVisible = false
                            binding.tvNotResult.isVisible = false
                        }

                        is WordContentUiState.Success -> {
                            wordAdapter.submitList(state.searchList)
                            binding.rvWord.isVisible = state.searchList.isNotEmpty()
                            binding.tvNotResult.isVisible = state.searchList.isEmpty()
                        }

                        is WordContentUiState.Empty -> {
                            binding.rvWord.isVisible = false
                            binding.tvNotResult.isVisible = true
                            wordAdapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
