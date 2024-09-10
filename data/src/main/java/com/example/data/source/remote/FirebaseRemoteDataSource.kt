package com.example.data.source.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseRemoteDataSource {


    suspend fun login(
        id: String,
        password: String
    ): Task<AuthResult>

    suspend fun logout(): Boolean


    suspend fun register(
        id: String,
        password: String
    ): Task<AuthResult>


    suspend fun resetPass(
        resetPassToId: String
    ): Task<Void>

    suspend fun delete(): Task<Void>?

    suspend fun createWordDB(
        id: String
    ): Task<Void>

    suspend fun addWordItem(
        id: String,
        wordItem: com.example.domain.model.BookmarkWord
    ): Task<Void>

    suspend fun deleteWordItem(
        id: String,
        wordItem: com.example.domain.model.BookmarkWord
    ): Task<Void>

    fun getFirebaseAuth(): FirebaseAuth

    fun getFirebaseFireStore(): FirebaseFirestore


}