package com.example.objectdetection.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.objectdetection.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.example.objectdetection.databinding.FragmentBookmarkBinding
import com.example.objectdetection.ext.showToast
import com.example.objectdetection.ui.adapter.BookmarkAdapter
import com.example.objectdetection.ui.home.HomeViewEvent
import com.example.objectdetection.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {

    private val homeViewModel by viewModels<HomeViewModel>()

    override val viewModel by viewModels<BookmarkViewModel>()

    private val bookmarkAdapter = BookmarkAdapter {
        homeViewModel.deleteBookmark(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    override fun initUi() {
        with(binding) {
            rvBookmark.adapter = bookmarkAdapter
            switchMean.setOnCheckedChangeListener { _, isShowMean ->
                bookmarkAdapter.toggleMean(isShowMean)
            }
        }
        viewModel.viewState.map(::onChangedViewState).launchIn(lifecycleScope)
        homeViewModel.viewEvent.map(::onChangeViewEvent).launchIn(lifecycleScope)

    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is BookmarkViewState.EmptyBookmarkList -> {
                binding.rvBookmark.isVisible = false
                binding.notBookmark.isVisible = true
            }

            is BookmarkViewState.ShowToast -> {
                showToast(message = state.message)
            }

            is BookmarkViewState.GetBookmarkList -> {
                binding.rvBookmark.isVisible = true
                binding.notBookmark.isVisible = false
                bookmarkAdapter.addAll(state.list)
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is HomeViewEvent.DeleteBookmark -> {
                bookmarkAdapter.delete(event.item)
            }

            is HomeViewEvent.AddBookmark -> {}
            is HomeViewEvent.ShowToast -> {}
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getBookmarkList()
    }
}