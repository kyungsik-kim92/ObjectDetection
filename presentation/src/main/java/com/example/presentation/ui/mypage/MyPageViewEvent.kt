package com.example.presentation.ui.mypage

import com.example.model.BookmarkWord
import com.example.presentation.base.ViewEvent
import com.example.presentation.base.ViewState
import com.prolificinteractive.materialcalendarview.CalendarDay


data class MyPageViewState(
    val bookmarkList: List<BookmarkWord> = emptyList(),
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