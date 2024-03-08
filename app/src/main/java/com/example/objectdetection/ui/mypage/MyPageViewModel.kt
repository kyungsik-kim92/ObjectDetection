package com.example.objectdetection.ui.mypage

import android.app.Application
import androidx.databinding.ObservableField
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.ext.getWordList
import com.example.objectdetection.ext.ioScope
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    app: Application,
    private val firebaseRepository: FirebaseRepository

) : BaseViewModel(app) {
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
        viewStateChanged(MyPageViewState.ShowProgress)
        ioScope {
            firebaseRepository.getWordList { list ->
                if (!list.isNullOrEmpty()) {
                    viewStateChanged(MyPageViewState.GetBookmarkList(list))

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
                        viewStateChanged(MyPageViewState.GetCalendarList(calendarDayList))
                    } else {
                        viewStateChanged(MyPageViewState.EmptyBookmarkList)
                    }
                } else {
                    viewStateChanged(MyPageViewState.EmptyBookmarkList)
                }
            }
        }
        viewStateChanged(MyPageViewState.HideProgress)
    }

    fun logout() {
        ioScope {
            if (firebaseRepository.logout()) {
                viewStateChanged(MyPageViewState.Logout)
            } else {
                viewStateChanged(MyPageViewState.ShowToast("로그아웃을 실패하였습니다."))
            }
        }
    }

    fun showWithdrawDialog() {
        viewStateChanged(MyPageViewState.ShowWithdrawDialog)
    }

    fun showLogoutDialog() {
        viewStateChanged(MyPageViewState.ShowLogoutDialog)
    }
}