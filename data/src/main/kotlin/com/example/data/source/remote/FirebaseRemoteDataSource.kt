package com.example.data.source.remote

import com.example.model.BookmarkWord
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseRemoteDataSource {

    fun login(
        id: String,
        password: String
    ): Task<AuthResult>

     fun logout(): Boolean


    fun register(
        id: String,
        password: String
    ): Task<AuthResult>


     fun resetPass(
        resetPassToId: String
    ): Task<Void>

     fun delete(): Task<Void>?

     fun createWordDB(
        id: String
    ): Task<Void>

     fun addWordItem(
        id: String,
        wordItem: BookmarkWord
    ): Task<Void>

     fun deleteWordItem(
        id: String,
        wordItem: BookmarkWord
    ): Task<Void>

    fun getFirebaseAuth(): FirebaseAuth

    fun getFirebaseFireStore(): FirebaseFirestore


}