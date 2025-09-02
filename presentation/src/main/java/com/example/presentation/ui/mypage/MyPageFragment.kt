package com.example.presentation.ui.mypage

import android.content.Intent
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
import com.example.presentation.MainActivity
import com.example.presentation.databinding.FragmentMyPageBinding
import com.example.presentation.ui.dialog.ChooseDialogFactory
import com.example.presentation.ui.dialog.ChooseDialogType
import com.example.presentation.util.EventDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.viewModel = myPageViewModel
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
                myPageViewModel.uiState.collect { state ->
                    when (state) {
                        is MyPageUiState.Loading -> {
                            binding.progressbar.isVisible = true
                        }

                        is MyPageUiState.Success -> {
                            binding.progressbar.isVisible = false
                            updateUserInfo(state)
                            updateCalendar(state)
                        }
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myPageViewModel.uiEvent.collect { event ->
                    when (event) {
                        is MyPageUiEvent.ShowWithdrawDialog -> {
                            ChooseDialogFactory(
                                ChooseDialogType.WithDraw,
                                childFragmentManager,
                                ::routeLogin
                            )
                        }

                        is MyPageUiEvent.ShowLogoutDialog -> {
                            ChooseDialogFactory(
                                ChooseDialogType.Logout,
                                childFragmentManager,
                                myPageViewModel::logout
                            )
                        }

                        is MyPageUiEvent.Logout -> {
                            logout()
                        }

                        is MyPageUiEvent.ShowToast -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is MyPageUiEvent.WithDraw -> {}
                    }
                }
            }
        }
    }


    private fun updateUserInfo(state: MyPageUiState.Success) {
        state.currentUser?.let { user ->
            val formatter = SimpleDateFormat("yyyy.MM.dd")
            val createTime = formatter.format(Date(user.metadata!!.creationTimestamp))
            binding.email.text = user.email
            binding.register.text = createTime
        }
    }

    private fun updateCalendar(state: MyPageUiState.Success) {
        binding.calendarView.removeDecorators()
        if (state.calendarList.isNotEmpty()) {
            state.calendarList.forEach { (calendarDay, count) ->
                binding.calendarView.addDecorator(
                    EventDecorator(
                        listOf(calendarDay),
                        requireActivity(),
                        count.convertLevel()
                    )
                )
            }
        }
    }

    private fun logout() {
        startActivity(Intent(requireActivity(), MainActivity::class.java).apply {
            putExtra(KEY_LOGOUT, true)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    private fun routeLogin() {
        startActivity(Intent(requireActivity(), MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun Int.convertLevel(): String {
        return when (this) {
            0 -> "level_0"
            in 1..3 -> "level_1"
            in 4..6 -> "level_2"
            else -> "level_3"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_LOGOUT = "key_logout"
    }
}