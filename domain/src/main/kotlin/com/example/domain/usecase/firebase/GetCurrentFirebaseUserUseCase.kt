package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import javax.inject.Inject

class GetCurrentFirebaseUserUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.getFirebaseAuth().currentUser
}