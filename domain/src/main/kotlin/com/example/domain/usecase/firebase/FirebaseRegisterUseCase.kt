package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.ext.toCallbackFlow
import javax.inject.Inject

class FirebaseRegisterUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke(email: String, password: String) =
        firebaseRepository.register(email, password).toCallbackFlow()
}