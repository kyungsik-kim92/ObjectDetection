package com.example.domain.repo

import com.example.domain.model.BookmarkWord

interface FirebaseRepository {
    suspend fun login(id: String, password: String)
    suspend fun logout()
    suspend fun register(id: String, password: String)
    suspend fun resetPass(resetPassToId: String)
    suspend fun delete()
    suspend fun createWordDB(id: String)
    suspend fun addWordItem(id: String, wordItem: BookmarkWord)
    suspend fun deleteWordItem(id: String, wordItem: BookmarkWord)
    fun getFirebaseAuth()
    fun getFirebaseFireStore() {
    }
}