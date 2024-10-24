package com.example.presentation.ui.mypage

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.firebase.FirebaseLogoutUseCase
import com.example.domain.usecase.firebase.GetWordListUseCase
import com.example.presentation.base.BaseViewModel
import com.example.presentation.base.ViewEvent
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val firebaseLogoutUseCase: FirebaseLogoutUseCase,
    private val getWordListUseCase: GetWordListUseCase,
    firebaseRepository: FirebaseRepository

) : BaseViewModel() {
    private val authListener = FirebaseAuth.AuthStateListener {
        it.currentUser?.let { currentUser ->
            val formatter = SimpleDateFormat("yyyy.MM.dd")
            val createTime =
                formatter.format(Date(currentUser.metadata!!.creationTimestamp))
            registerDateObservableField.set(createTime)
            emailObservableField.set(currentUser.email)
        }
    }

    val emailObservableField = ObservableField("")
    val registerDateObservableField = ObservableField("")

    init {
        firebaseRepository.getFirebaseAuth().addAuthStateListener(authListener)
    }

    fun getBookmarkList() {
        getWordListUseCase().onStart {
            onChangedViewState(MyPageViewState(isLoading = true))
        }.map { wordList ->
            val calendarDayList = mutableListOf<Pair<CalendarDay, Int>>()
            val toCalendarDayList =
                wordList.groupBy { it.year }.map { it.value.groupBy { it.month } }
                    .map { it.entries.map { it.value.groupBy { it.day } } }
            toCalendarDayList.forEach { yearGroup ->
                yearGroup.forEach { monthGroup ->
                    monthGroup.forEach {
                        val calendarDay = CalendarDay.from(
                            it.value[0].year.toInt(),
                            it.value[0].month.toInt(),
                            it.value[0].day.toInt()
                        )
                        val getCount = it.value.size
                        calendarDayList.add(Pair(calendarDay, getCount))
                    }
                }
            }
            onChangedViewState(
                MyPageViewState(
                    bookmarkList = wordList,
                    calendarList = calendarDayList,
                    isLoading = false
                )
            )
        }.launchIn(viewModelScope)
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
}