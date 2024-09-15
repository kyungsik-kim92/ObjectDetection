package com.example.data.ext

import com.example.model.BookmarkWord
import com.example.model.toBookmarkWord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun com.example.data.repo.FirebaseRepository.getWordList(callback: (list: List<BookmarkWord>?) -> Unit) {

    getFirebaseAuth().currentUser?.email?.let { userId ->
        getFirebaseFireStore().collection(userId).document("word").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.exists()) {
                        val getResult: ArrayList<HashMap<String, String>>? =
                            it.result.get("list") as ArrayList<HashMap<String, String>>?
                        val toResultList = getResult?.map { it.toBookmarkWord() }
                        if (!toResultList.isNullOrEmpty()) {
                            callback(toResultList)
                        } else {
                            callback(null)
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            createWordDB { isCreate ->
                                if (isCreate) {
                                    callback(emptyList())
                                } else {
                                    callback(null)
                                }
                            }
                        }
                    }
                } else {
                    callback(null)
                }
            }
    }
}


suspend fun com.example.data.repo.FirebaseRepository.createWordDB(callback: (isSuccess: Boolean) -> Unit) {
    getFirebaseAuth().currentUser?.email?.let { userId ->
        createWordDB(userId).addOnCompleteListener { dbResult ->
            callback(dbResult.isSuccessful)
        }
    }
}


suspend fun com.example.data.repo.FirebaseRepository.addWord(
    word: BookmarkWord,
    callback: (isSuccess: Boolean) -> Unit
) {
    getFirebaseAuth().currentUser?.email?.let { userId ->
        addWordItem(id = userId, word).addOnCompleteListener { dbResult ->
            callback(dbResult.isSuccessful)
        }
    }
}

suspend fun com.example.data.repo.FirebaseRepository.deleteWord(
    word: BookmarkWord,
    callback: (isSuccess: Boolean) -> Unit
) {
    getFirebaseAuth().currentUser?.email?.let { userId ->
        deleteWordItem(userId, word).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }
}
