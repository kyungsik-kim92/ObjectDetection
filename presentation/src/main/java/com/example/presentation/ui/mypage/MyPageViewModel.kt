package com.example.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.FirebaseLogoutUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.firebase.GetCurrentFirebaseUserUseCase
import com.example.domain.model.BookmarkWord
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val currentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase,
    private val firebaseLogoutUseCase: FirebaseLogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
    val uiEvent: SharedFlow<MyPageUiEvent> = _uiEvent.asSharedFlow()


    init {
        viewModelScope.launch {
            _uiState.value = MyPageUiState.Loading
            getBookmarkWordListUseCase().collect { wordList ->
                _uiState.value = MyPageUiState.Success(
                    currentUser = currentFirebaseUserUseCase(),
                    calendarList = wordList.toCalendarDayList()
                )
            }
        }
    }

    fun logout() {
        if (firebaseLogoutUseCase()) {
            viewModelScope.launch {
                _uiEvent.emit(MyPageUiEvent.Logout)
            }
        } else {
            viewModelScope.launch {
                _uiEvent.emit(MyPageUiEvent.ShowToast("로그아웃을 실패하였습니다."))
            }
        }
    }

    fun showWithdrawDialog() {
        viewModelScope.launch {
            _uiEvent.emit(MyPageUiEvent.ShowWithdrawDialog)
        }
    }

    fun showLogoutDialog() {
        viewModelScope.launch {
            _uiEvent.emit(MyPageUiEvent.ShowLogoutDialog)
        }
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
