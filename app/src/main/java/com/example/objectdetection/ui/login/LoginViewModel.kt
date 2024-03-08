package com.example.objectdetection.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.objectdetection.base.BaseViewModel
import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    app: Application,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel(app){

    val inputEmailLiveData = MutableLiveData<String>()
    val inputPasswordLiveData = MutableLiveData<String>()



    fun login() {
        ioScope {
            viewStateChanged(LoginViewState.ShowProgress)
            viewStateChanged(LoginViewState.EnableInput(false))

            val checkEmail = async { checkEmail() }
            val checkPassword = async { checkPassword() }

            checkUser(checkEmail.await(), checkPassword.await())?.let { person ->
                firebaseRepository.login(person.email, person.password)
                    .addOnSuccessListener {
                        viewStateChanged(LoginViewState.RouteHome)
                        viewStateChanged(LoginViewState.HideProgress)
                    }.addOnFailureListener {
                        viewStateChanged(LoginViewState.Error("로그인을 실패하였습니다."))
                        viewStateChanged(LoginViewState.HideProgress)
                    }
            }
        }
    }


    fun register() {
        viewStateChanged(LoginViewState.RouteRegister)
    }


    private fun checkUser(
        checkEmail: Boolean,
        checkPassword: Boolean
    ): Person? {
        return if (checkEmail && checkPassword) {
            Person(
                inputEmailLiveData.value!!,
                inputPasswordLiveData.value!!
            )
        } else {
            viewStateChanged(LoginViewState.EnableInput(true))
            viewStateChanged(LoginViewState.HideProgress)
            null
        }
    }

    private fun checkEmail(): Boolean {
        return when {
            inputEmailLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(LoginViewState.Error("이메일을 입력해주세요."))
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmailLiveData.value).matches() -> {
                viewStateChanged(LoginViewState.Error("이메일 형식이 올바르지 않습니다."))
                false
            }

            else -> true
        }
    }

    private fun checkPassword(): Boolean {
        return when {
            inputPasswordLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(LoginViewState.Error("비밀번호를 입력해주세요."))
                false
            }

            else -> true
        }
    }

    data class Person(
        val email: String,
        val password: String
    )
}