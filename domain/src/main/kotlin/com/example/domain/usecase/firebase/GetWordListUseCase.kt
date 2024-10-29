package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import com.example.model.BookmarkWord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetWordListUseCase @Inject constructor(
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke() = callbackFlow<List<BookmarkWord>> {
        firebaseRepository.getFirebaseFireStore()
            .collection(getCurrentFirebaseUserUseCase()?.email.orEmpty())
            .document("word")
            .addSnapshotListener { value, error ->
                if (error != null) trySend(emptyList())
                else if (value?.exists() == true) trySend(Gson().fromJson(value["list"]))
            }
        awaitClose()
    }


    private inline fun <reified T> Gson.fromJson(json: Any?): T =
        fromJson(toJson(json), object : TypeToken<T>() {}.type)

}