package com.example.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.presentation.MainActivity
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.example.presentation.databinding.FragmentMyPageBinding
import com.example.presentation.ui.dialog.ChooseDialog
import com.example.presentation.ui.dialog.ChooseItem
import com.example.presentation.ui.dialog.WithdrawDialog
import com.example.presentation.ui.login.LoginFragment
import com.example.presentation.util.EventDecorator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override val viewModel by viewModels<MyPageViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

    }


    override fun initUi() {}

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is MyPageViewState -> {
                if (state.bookmarkList.isNotEmpty()) {
                    state.calendarList.forEach {
                        binding.calendarView.addDecorator(
                            EventDecorator(
                                listOf(it.first),
                                requireActivity(),
                                it.second.convertLevel()
                            )
                        )
                    }
                } else {
                    binding.calendarView.removeDecorators()
                }
                binding.progressbar.isVisible = state.isLoading
            }

        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        super.onChangeViewEvent(event)
        when (event) {
            is MyPageViewEvent.ShowWithdrawDialog -> {
                ChooseDialog(
                    ChooseItem(
                        title = "회원탈퇴",
                        content = "회원탈퇴 하시겠습니까?",
                        negativeString = "취소",
                        positiveString = "탈퇴"
                    ),
                    dismissCallback = {
                        WithdrawDialog(
                            ChooseItem(
                                title = "회원탈퇴",
                                content = "",
                                negativeString = "취소",
                                positiveString = "탈퇴"
                            ),
                            dismissCallback = {
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        LoginFragment::class.java
                                    ).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    })
                            }
                        ).show(childFragmentManager, "WithdrawDialog")
                    }
                ).show(childFragmentManager, "ChooseDialog")
            }

            is MyPageViewEvent.ShowLogoutDialog -> {
                ChooseDialog(
                    ChooseItem(
                        title = "로그아웃",
                        content = "로그아웃 하시겠습니까?",
                        negativeString = "취소",
                        positiveString = "로그아웃"
                    ),
                    dismissCallback = {
                        viewModel.logout()
                    }
                ).show(childFragmentManager, "ChooseDialog")
            }

            is MyPageViewEvent.Logout -> {
                startActivity(Intent(requireActivity(), MainActivity::class.java).apply {
                    putExtra(KEY_LOGOUT, true)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBookmarkList()
    }

    private fun Int.convertLevel(): String {
        return when (this) {
            0 -> {
                "level_0"
            }

            in 1..3 -> {
                "level_1"
            }

            in 4..6 -> {
                "level_2"
            }

            else -> {
                "level_3"
            }
        }
    }

    companion object {
        const val KEY_LOGOUT = "key_logout"
    }
}