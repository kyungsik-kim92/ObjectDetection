package com.example.presentation.ui.bookmark

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
import com.example.presentation.databinding.FragmentBookmarkBinding
import com.example.presentation.ext.routeWordDetailFromMain
import com.example.presentation.ui.adapter.BookmarkAdapter
import com.example.presentation.ui.home.HomeUiEvent
import com.example.presentation.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private val bookmarkAdapter = BookmarkAdapter(
        onDelete = { bookmarkWord ->
            homeViewModel.deleteBookmark(bookmarkWord)
        },
        onItemClick = { bookmarkWord ->
            routeWordDetailFromMain(bookmarkWord.word)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiState()
        observeEvents()
    }

    private fun initUi() {
        with(binding) {
            rvBookmark.adapter = bookmarkAdapter
            switchMean.setOnCheckedChangeListener { _, isShowMean ->
                bookmarkAdapter.toggleMean(isShowMean)
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.uiState.collect { state ->
                    when (state) {
                        is BookmarkUiState.Loading -> {}

                        is BookmarkUiState.Success -> {
                            binding.rvBookmark.isVisible = true
                            binding.notBookmark.isVisible = false
                            bookmarkAdapter.submitList(state.bookmarkList)
                        }

                        is BookmarkUiState.Empty -> {
                            binding.rvBookmark.isVisible = false
                            binding.notBookmark.isVisible = true
                            bookmarkAdapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }


    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiEvent.collect { event ->
                    when (event) {
                        is HomeUiEvent.DeleteBookmark -> {
                            bookmarkAdapter.delete(event.item)
                        }

                        is HomeUiEvent.AddBookmark -> {}
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