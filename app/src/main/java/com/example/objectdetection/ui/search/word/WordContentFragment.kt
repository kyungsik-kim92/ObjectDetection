package com.example.objectdetection.ui.search.word

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.databinding.FragmentWordContentBinding
import com.example.objectdetection.ext.showToast
import com.example.objectdetection.ui.adapter.WordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordContentFragment :
    BaseFragment<FragmentWordContentBinding>(R.layout.fragment_word_content) {

    private val wordContentViewModel by viewModels<WordContentViewModel>()

    private val wordAdapter = WordAdapter()

    private var isStopAndDestroy = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {
        with(binding) {
            etSearch.addTextChangedListener {
                if (!it.isNullOrEmpty()) {
                    wordContentViewModel.searchWord(it.toString())
                }
            }
            rvWord.adapter = wordAdapter
        }


        wordAdapter.setOnItemClickListener { clickItem ->
            findNavController().navigate(
                R.id.action_content_to_detail,
                bundleOf(ARG_WORD to clickItem)
            )
        }
    }

    private fun initViewModel() {
        wordContentViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? WordContentViewState)?.let {
                onChangedWordContentViewState(it)
            }
        }

    }

    private fun onChangedWordContentViewState(viewState: WordContentViewState) {
        when (viewState) {

            is WordContentViewState.EmptyResult -> {
                binding.rvWord.isVisible = false
                binding.tvNotResult.isVisible = true
            }

            is WordContentViewState.GetSearchResult -> {
                binding.rvWord.isVisible = true
                binding.tvNotResult.isVisible = false
                wordAdapter.addAll(viewState.list)
            }

            is WordContentViewState.ShowToast -> {
                showToast(message = viewState.message)
            }

            is WordContentViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            is WordContentViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
        }
    }

    override fun onStop() {
        isStopAndDestroy = true
        super.onStop()
    }

    override fun onDestroy() {
        isStopAndDestroy = true
        super.onDestroy()
    }


    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 1500L
    }
}
