package com.example.presentation.ui.mypage

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.FirebaseLogoutUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.firebase.GetCurrentFirebaseUserUseCase
import com.example.model.BookmarkWord
import com.example.presentation.base.BaseViewModel
import com.example.presentation.base.ViewEvent
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    currentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase,
    private val firebaseLogoutUseCase: FirebaseLogoutUseCase,
) : BaseViewModel() {

    private val myPageViewState = MyPageViewState(
        currentUser = currentFirebaseUserUseCase(),
    )

    init {
        getBookmarkWordListUseCase().onStart {
            onChangedViewState(myPageViewState.copy(isLoading = true))
        }.map { wordList ->
            myPageViewState.copy(isLoading = false, calendarList = wordList.toCalendarDayList())
        }.onEach(::onChangedViewState).launchIn(viewModelScope)
    }

    fun logout() {
        if (firebaseLogoutUseCase()) {
            onChangedViewEvent(MyPageViewEvent.Logout)
        } else {
            onChangedViewEvent(ViewEvent.ShowToast("로그아웃을 실패하였습니다."))
        }
    }

    fun showWithdrawDialog() {
        onChangedViewEvent(MyPageViewEvent.ShowWithdrawDialog)
    }

    fun showLogoutDialog() {
        onChangedViewEvent(MyPageViewEvent.ShowLogoutDialog)
    }

    private fun List<BookmarkWord>.toCalendarDayList(): List<Pair<CalendarDay, Int>> =
        groupBy { Triple(it.year, it.month, it.day) }
            .map { (_, words) ->
                val (year, month, day) = words.first().let {
                    Triple(it.year.toInt(), it.month.toInt(), it.day.toInt())
                }
                CalendarDay.from(year, month, day) to words.size
            }
}
