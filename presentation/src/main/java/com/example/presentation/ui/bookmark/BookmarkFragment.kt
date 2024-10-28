package com.example.presentation.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentBookmarkBinding
import com.example.presentation.ui.adapter.BookmarkAdapter
import com.example.presentation.ui.home.HomeViewEvent
import com.example.presentation.ui.home.HomeViewModel
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
        homeViewModel.viewEvent.map(::onChangeViewEvent).launchIn(lifecycleScope)

    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is BookmarkViewState -> {
                binding.rvBookmark.isVisible = state.bookmarkList.isNotEmpty()
                binding.notBookmark.isVisible = state.bookmarkList.isNotEmpty()
                bookmarkAdapter.addAll(state.bookmarkList)
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is HomeViewEvent.DeleteBookmark -> {
                bookmarkAdapter.delete(event.item)
            }

            is HomeViewEvent.AddBookmark -> {}

        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getBookmarkList()
    }
}