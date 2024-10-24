package com.example.data.repo

import com.example.data.source.remote.FirebaseRemoteDataSource
import com.example.domain.repo.FirebaseRepository
import com.example.model.BookmarkWord
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebaseRepository {
    override fun login(id: String, password: String): Task<AuthResult> =
        firebaseRemoteDataSource.login(id, password)


    override  fun logout(): Boolean = firebaseRemoteDataSource.logout()

    override fun register(id: String, password: String): Task<AuthResult> =
        firebaseRemoteDataSource.register(id, password)

    override  fun resetPass(resetPassToId: String): Task<Void> =
        firebaseRemoteDataSource.resetPass(resetPassToId)

    override  fun delete(): Task<Void>? =
        firebaseRemoteDataSource.delete()

    override  fun createWordDB(id: String): Task<Void> =
        firebaseRemoteDataSource.createWordDB(id)

    override  fun addWordItem(id: String, wordItem: BookmarkWord): Task<Void> =
        firebaseRemoteDataSource.addWordItem(id, wordItem)

    override  fun deleteWordItem(id: String, wordItem: BookmarkWord): Task<Void> =
        firebaseRemoteDataSource.deleteWordItem(id, wordItem)

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseRemoteDataSource.getFirebaseAuth()

    override fun getFirebaseFireStore(): FirebaseFirestore =
        firebaseRemoteDataSource.getFirebaseFireStore()
}