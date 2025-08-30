package com.example.domain.usecase.firebase

import com.example.domain.repo.FirebaseRepository
import com.example.model.BookmarkWord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetBookmarkWordListUseCase @Inject constructor(
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke() = callbackFlow<List<BookmarkWord>> {
        val currentUser = getCurrentFirebaseUserUseCase()

        if (currentUser == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        firebaseRepository.getFirebaseFireStore()
            .collection(currentUser.email ?: "")
            .document("word")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(emptyList())
                } else if (value?.exists() == true) {
                    try {
                        val list = value["list"]
                        if (list != null) {
                            trySend(Gson().fromJson<List<BookmarkWord>>(list))
                        } else {
                            trySend(emptyList())
                        }
                    } catch (e: Exception) {
                        trySend(emptyList())
                    }
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose()
    }


    private inline fun <reified T> Gson.fromJson(json: Any?): T =
        fromJson(toJson(json), object : TypeToken<T>() {}.type)

}