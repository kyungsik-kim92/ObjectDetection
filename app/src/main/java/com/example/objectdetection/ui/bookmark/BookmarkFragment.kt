package com.example.objectdetection.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.databinding.FragmentBookmarkBinding
import com.example.objectdetection.ext.showToast
import com.example.objectdetection.ui.adapter.BookmarkAdapter
import com.example.objectdetection.ui.home.HomeViewModel
import com.example.objectdetection.ui.home.HomeViewState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private val bookmarkAdapter = BookmarkAdapter{
        homeViewModel.deleteBookmark(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {
        with(binding) {
            rvBookmark.adapter = bookmarkAdapter
            switchMean.setOnCheckedChangeListener { _, isShowMean ->
                bookmarkAdapter.toggleMean(isShowMean)
            }
        }

    }

    private fun initViewModel() {
        homeViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? HomeViewState)?.let {
                onChangedHomeViewState(it)
            }
        }


        bookmarkViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? BookmarkViewState)?.let {
                onChangedBookmarkViewState(it)
            }
        }
    }

    private fun onChangedHomeViewState(viewState: HomeViewState) {
        when (viewState) {
            is HomeViewState.DeleteBookmark -> {
                bookmarkAdapter.delete(viewState.item)
            }

            is HomeViewState.AddBookmark -> {}
            is HomeViewState.ShowToast -> {}
        }
    }

    private fun onChangedBookmarkViewState(viewState: BookmarkViewState) {
        when (viewState) {
            is BookmarkViewState.EmptyBookmarkList -> {
                binding.rvBookmark.isVisible = false
                binding.notBookmark.isVisible = true
            }

            is BookmarkViewState.ShowToast -> {
                showToast(message = viewState.message)
            }

            is BookmarkViewState.GetBookmarkList -> {
                binding.rvBookmark.isVisible = true
                binding.notBookmark.isVisible = false
                bookmarkAdapter.addAll(viewState.list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bookmarkViewModel.getBookmarkList()
    }
}