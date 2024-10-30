package com.example.presentation.ui.mypage

import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.google.firebase.auth.FirebaseUser
import com.prolificinteractive.materialcalendarview.CalendarDay


data class MyPageViewState(
    val currentUser: FirebaseUser? = null,
    val calendarList: List<Pair<CalendarDay, Int>> = emptyList(),
    val isLoading: Boolean = false
) : ViewState


sealed class MyPageViewEvent : ViewEvent {
    data object Logout : MyPageViewEvent()
    data object WithDraw : MyPageViewEvent()
    data object ShowWithdrawDialog : MyPageViewEvent()
    data object ShowLogoutDialog : MyPageViewEvent()
    data class ShowToast(val message: String) : MyPageViewEvent()
}