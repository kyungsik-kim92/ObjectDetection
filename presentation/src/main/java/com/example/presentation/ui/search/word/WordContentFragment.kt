package com.example.presentation.ui.search.word

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentWordContentBinding
import com.example.presentation.ui.adapter.WordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordContentFragment :
    BaseFragment<FragmentWordContentBinding>(R.layout.fragment_word_content) {


    override val viewModel by viewModels<WordContentViewModel>()
    private val wordAdapter = WordAdapter()

    override fun initUi() {
        binding.rvWord.adapter = wordAdapter
        wordAdapter.setOnItemClickListener { clickItem ->
            findNavController().navigate(
                R.id.action_content_to_detail,
                bundleOf(ARG_WORD to clickItem)
            )
        }
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {

            is WordContentViewState -> {
                wordAdapter.addAll(state.searchList)
                binding.rvWord.isVisible = state.searchList.isNotEmpty()
                binding.tvNotResult.isVisible = state.searchList.isEmpty()
            }
        }
    }
}
