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
import com.example.presentation.ui.dialog.ChooseDialogFactory
import com.example.presentation.ui.dialog.ChooseDialogType
import com.example.presentation.ui.login.LoginFragment
import com.example.presentation.util.EventDecorator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date


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
                with(binding) {
                    state.currentUser?.let {
                        val formatter = SimpleDateFormat("yyyy.MM.dd")
                        val createTime =
                            formatter.format(Date(it.metadata!!.creationTimestamp))
                        email.text = it.email
                        register.text = createTime
                    }
                    if (state.calendarList.isNotEmpty()) {
                        state.calendarList.forEach {
                            calendarView.addDecorator(
                                EventDecorator(
                                    listOf(it.first),
                                    requireActivity(),
                                    it.second.convertLevel()
                                )
                            )
                        }
                    } else {
                        calendarView.removeDecorators()
                    }
                    progressbar.isVisible = state.isLoading
                }
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        super.onChangeViewEvent(event)
        when (event) {
            is MyPageViewEvent.ShowWithdrawDialog -> {
                ChooseDialogFactory(ChooseDialogType.WithDraw, childFragmentManager, ::routeLogin)
            }

            is MyPageViewEvent.ShowLogoutDialog -> {
                ChooseDialogFactory(
                    ChooseDialogType.Logout,
                    childFragmentManager,
                    viewModel::logout
                )
            }

            is MyPageViewEvent.Logout -> {
                logout()
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
        startActivity(
            Intent(
                requireActivity(),
                LoginFragment::class.java
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
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