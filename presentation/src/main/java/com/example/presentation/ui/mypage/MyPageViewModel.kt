package com.example.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.firebase.FirebaseLogoutUseCase
import com.example.domain.usecase.firebase.GetBookmarkWordListUseCase
import com.example.domain.usecase.firebase.GetCurrentFirebaseUserUseCase
import com.example.model.BookmarkWord
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    currentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    getBookmarkWordListUseCase: GetBookmarkWordListUseCase,
    private val firebaseLogoutUseCase: FirebaseLogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
    val uiEvent: SharedFlow<MyPageUiEvent> = _uiEvent.asSharedFlow()

    private val currentUser = currentFirebaseUserUseCase()

    init {
        getBookmarkWordListUseCase()
            .onStart {
                _uiState.value = MyPageUiState.Loading
            }
            .map { wordList ->
                MyPageUiState.Success(
                    currentUser = currentUser,
                    calendarList = wordList.toCalendarDayList()
                )
            }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
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
