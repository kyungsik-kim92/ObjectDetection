package com.example.presentation.ui.mypage

import com.google.firebase.auth.FirebaseUser
import com.prolificinteractive.materialcalendarview.CalendarDay


sealed class MyPageUiState {
    data object Loading : MyPageUiState()
    data class Success(
        val currentUser: FirebaseUser? = null,
        val calendarList: List<Pair<CalendarDay, Int>> = emptyList()
    ) : MyPageUiState()
}

sealed class MyPageUiEvent {
    data object Logout : MyPageUiEvent()
    data object WithDraw : MyPageUiEvent()
    data object ShowWithdrawDialog : MyPageUiEvent()
    data object ShowLogoutDialog : MyPageUiEvent()
    data class ShowToast(val message: String) : MyPageUiEvent()
}