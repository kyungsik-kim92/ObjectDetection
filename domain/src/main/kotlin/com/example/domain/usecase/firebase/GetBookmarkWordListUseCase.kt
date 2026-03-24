package com.example.domain.usecase.firebase

import com.example.domain.model.BookmarkWord
import com.example.domain.repo.FirebaseRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class GetBookmarkWordListUseCase @Inject constructor(
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase,
    private val firebaseRepository: FirebaseRepository,
    private val json: Json
) {
    operator fun invoke() = callbackFlow<List<BookmarkWord>> {
        val currentUser = getCurrentFirebaseUserUseCase()

        if (currentUser == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration = firebaseRepository.getFirebaseFireStore()
            .collection(currentUser.email ?: "")
            .document("word")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val rawList = snapshot?.get("list")
                trySend(rawList.toBookmarkWords(json))
            }
        awaitClose { registration.remove() }
    }


    private fun Any?.toBookmarkWords(json: Json): List<BookmarkWord> {
        val items = this as? List<*> ?: return emptyList()
        return items.mapNotNull { item ->
            val map = item as? Map<*, *> ?: return@mapNotNull null
            runCatching {
                val jsonObject = JsonObject(
                    map.entries.associate { (k, v) ->
                        k.toString() to JsonPrimitive(v?.toString().orEmpty())
                    }
                )
                json.decodeFromJsonElement(BookmarkWord.serializer(), jsonObject)
            }.getOrNull()
        }
    }
}