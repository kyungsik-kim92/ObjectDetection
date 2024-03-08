package com.example.objectdetection.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.databinding.FragmentMyPageBinding
import com.example.objectdetection.ui.dialog.ChooseDialog
import com.example.objectdetection.ui.dialog.ChooseItem
import com.example.objectdetection.ui.dialog.WithdrawDialog
import com.example.objectdetection.ui.login.LoginActivity
import com.example.objectdetection.util.EventDecorator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageFragment :BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {

    }

    private fun initViewModel() {
        binding.viewModel = myPageViewModel

        myPageViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? MyPageViewState)?.let {
                onChangedMyPageViewState(it)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        myPageViewModel.getBookmarkList()
    }

    private fun onChangedMyPageViewState(viewState: MyPageViewState) {
        when (viewState) {
            is MyPageViewState.GetBookmarkList -> {
                viewState.list
            }

            is MyPageViewState.GetCalendarList -> {

                viewState.list.forEach {
                    binding.calendarView.addDecorator(
                        EventDecorator(
                            listOf(it.first),
                            requireActivity(),
                            it.second.convertLevel()
                        )
                    )
                }
            }

            is MyPageViewState.ShowLogoutDialog -> {
                ChooseDialog(
                    ChooseItem(
                        title = "로그아웃",
                        content = "로그아웃 하시겠습니까?",
                        negativeString = "취소",
                        positiveString = "로그아웃"
                    ),
                    dismissCallback = {
                        myPageViewModel.logout()
                    }
                ).show(childFragmentManager, "ChooseDialog")
            }

            is MyPageViewState.ShowWithdrawDialog -> {
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
                                        LoginActivity::class.java
                                    ).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    })
                            }
                        ).show(childFragmentManager, "WithdrawDialog")
                    }
                ).show(childFragmentManager, "ChooseDialog")
            }

            is MyPageViewState.Logout -> {
                startActivity(Intent(requireActivity(), LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            }

            is MyPageViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            is MyPageViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }

            is MyPageViewState.EmptyBookmarkList -> {
                binding.calendarView.removeDecorators()
            }

            is MyPageViewState.ShowToast -> {

            }
            MyPageViewState.WithDraw -> {

            }
        }
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
}