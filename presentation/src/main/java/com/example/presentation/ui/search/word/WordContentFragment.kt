package com.example.presentation.ui.search.word

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentWordContentBinding
import com.example.presentation.ext.routeWordDetail
import com.example.presentation.ui.adapter.WordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordContentFragment :
    BaseFragment<FragmentWordContentBinding>(R.layout.fragment_word_content) {


    override val viewModel by viewModels<WordContentViewModel>()
    private val wordAdapter = WordAdapter(::routeWordDetail)

    override fun initUi() {
        binding.rvWord.adapter = wordAdapter
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is WordContentViewState -> {
                wordAdapter.submitList(state.searchList)
                binding.rvWord.isVisible = state.searchList.isNotEmpty()
                binding.tvNotResult.isVisible = state.searchList.isEmpty()
            }
        }
    }
}
