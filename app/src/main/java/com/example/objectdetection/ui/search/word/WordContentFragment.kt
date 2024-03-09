package com.example.objectdetection.ui.search.word

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.base.ViewEvent
import com.example.objectdetection.base.ViewState
import com.example.objectdetection.databinding.FragmentWordContentBinding
import com.example.objectdetection.ext.showToast
import com.example.objectdetection.ui.adapter.WordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class WordContentFragment :
    BaseFragment<FragmentWordContentBinding>(R.layout.fragment_word_content) {


    override val viewModel by viewModels<WordContentViewModel>()

    private val wordAdapter = WordAdapter()

    private var isStopAndDestroy = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

    }


    override fun initUi() {
        with(binding) {
            etSearch.addTextChangedListener {
                if (!it.isNullOrEmpty()) {
                    viewModel.searchWord(it.toString())
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

        viewModel.viewState.map(::onChangedViewState).launchIn(lifecycleScope)
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {

            is WordContentViewState.EmptyResult -> {
                binding.rvWord.isVisible = false
                binding.tvNotResult.isVisible = true
            }

            is WordContentViewState.GetSearchResult -> {
                binding.rvWord.isVisible = true
                binding.tvNotResult.isVisible = false
                wordAdapter.addAll(state.list)
            }

            is WordContentViewState.ShowToast -> {
                showToast(message = state.message)
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

    override fun onChangeViewEvent(event: ViewEvent) {

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
