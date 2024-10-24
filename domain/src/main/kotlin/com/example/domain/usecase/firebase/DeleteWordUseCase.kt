package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import com.example.domain.usecase.ext.toVoidCallbackFlow
import com.example.model.BookmarkWord
import javax.inject.Inject

class DeleteWordUseCase @Inject constructor(
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(item: BookmarkWord) =
        firebaseRepository.deleteWordItem(getCurrentFirebaseUserUseCase()?.email.orEmpty(), item)
            .toVoidCallbackFlow()

}

