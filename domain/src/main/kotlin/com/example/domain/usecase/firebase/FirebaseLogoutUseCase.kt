package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import javax.inject.Inject

class FirebaseLogoutUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.logout()
}