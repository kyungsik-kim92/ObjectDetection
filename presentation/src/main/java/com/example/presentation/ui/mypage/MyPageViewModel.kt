package com.example.presentation.ui.mypage

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.presentation.base.BaseViewModel
import com.example.data.ext.getWordList
import com.example.domain.repo.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository

) : BaseViewModel() {
    private val auth = FirebaseAuth.getInstance()

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
        onChangedViewState(MyPageViewState.ShowProgress)
        viewModelScope.launch(Dispatchers.IO){
            firebaseRepository.getWordList { list ->
                if (!list.isNullOrEmpty()) {
                    onChangedViewState(MyPageViewState.GetBookmarkList(list))

                    val calendarDayList = mutableListOf<Pair<CalendarDay, Int>>()

                    val toCalendarDayList =
                        list.groupBy { it.year }.map { it.value.groupBy { it.month } }
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

                    if (calendarDayList.isNotEmpty()) {
                        onChangedViewState(MyPageViewState.GetCalendarList(calendarDayList))
                    } else {
                        onChangedViewState(MyPageViewState.EmptyBookmarkList)
                    }
                } else {
                    onChangedViewState(MyPageViewState.EmptyBookmarkList)
                }
            }
        }
        onChangedViewState(MyPageViewState.HideProgress)
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            if (firebaseRepository.logout()) {
                onChangedViewState(MyPageViewState.Logout)
            } else {
                onChangedViewState(MyPageViewState.ShowToast("로그아웃을 실패하였습니다."))
            }
        }
    }

    fun showWithdrawDialog() {
        onChangedViewState(MyPageViewState.ShowWithdrawDialog)
    }

    fun showLogoutDialog() {
        onChangedViewState(MyPageViewState.ShowLogoutDialog)
    }
}