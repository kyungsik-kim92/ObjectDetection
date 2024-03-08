package com.example.objectdetection.ui.mypage

import com.example.objectdetection.base.ViewState
import com.example.objectdetection.model.BookmarkWord
import com.prolificinteractive.materialcalendarview.CalendarDay

sealed class MyPageViewState : ViewState {

    object Logout : MyPageViewState()
    object WithDraw : MyPageViewState()

    object ShowWithdrawDialog : MyPageViewState()
    object ShowLogoutDialog : MyPageViewState()

    data class GetBookmarkList(val list: List<BookmarkWord>) : MyPageViewState()

    data class GetCalendarList(val list: List<Pair<CalendarDay, Int>>) : MyPageViewState()
    object EmptyBookmarkList : MyPageViewState()
    data class ShowToast(val message: String) : MyPageViewState()

    object ShowProgress : MyPageViewState()
    object HideProgress : MyPageViewState()
}