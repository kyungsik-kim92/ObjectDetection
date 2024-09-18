package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseLoginUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(email: String, password: String) = callbackFlow {
        firebaseRepository.login(email, password)
            .addOnSuccessListener {
                trySend(true)
            }.addOnFailureListener {
                trySend(false)
            }
    }
}