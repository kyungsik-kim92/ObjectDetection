package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.ext.toVoidCallbackFlow
import javax.inject.Inject

class FirebaseLoginUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(email: String, password: String) =
        firebaseRepository.login(email, password).toVoidCallbackFlow()

}