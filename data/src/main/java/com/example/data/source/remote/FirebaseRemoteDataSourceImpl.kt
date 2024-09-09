package com.example.data.source.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : FirebaseRemoteDataSource {
    override suspend fun login(id: String, password: String): Task<AuthResult> =
        firebaseAuth.signInWithEmailAndPassword(id, password)

    override suspend fun logout(): Boolean {
        firebaseAuth.signOut()
        return firebaseAuth.currentUser == null
    }

    override suspend fun register(id: String, password: String): Task<AuthResult> =
        firebaseAuth.createUserWithEmailAndPassword(id, password)

    override suspend fun resetPass(resetPassToId: String): Task<Void> =
        firebaseAuth.sendPasswordResetEmail(resetPassToId)

    override suspend fun delete(): Task<Void>? =
        firebaseAuth.currentUser?.delete()

    override suspend fun createWordDB(id: String): Task<Void> =
        fireStore.collection(id).document("word")
            .set(emptyMap<String, com.example.domain.model.BookmarkWord>())

    override suspend fun addWordItem(
        id: String,
        wordItem: com.example.domain.model.BookmarkWord
    ): Task<Void> =
        fireStore.collection(id).document("word").update("list", FieldValue.arrayUnion(wordItem))

    override suspend fun deleteWordItem(
        id: String,
        wordItem: com.example.domain.model.BookmarkWord
    ): Task<Void> =
        fireStore.collection(id).document("word").update("list", FieldValue.arrayRemove(wordItem))


    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseAuth

    override fun getFirebaseFireStore(): FirebaseFirestore =
        fireStore
}