package com.example.ziadartwork.data.implementations

import android.util.Log
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.repository.PaintingsRepository
import com.example.ziadartwork.ui.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class PaintingsRepoImpl @Inject constructor() : PaintingsRepository {
    //TODO should depend on abstration and not paintingRef directly. Replace static strings
    private val paintingRef: CollectionReference = Firebase.firestore.collection("paintings")

    companion object {
        val TAG = PaintingsRepoImpl::class.simpleName
    }

    private var paintingsList: MutableList<Painting> = mutableListOf<Painting>()

    override fun getAllPaintings(): Flow<Result<List<Painting>>> = callbackFlow {
        Log.d(TAG, "getAllPaintings: START")

        val snapshotListener = paintingRef.addSnapshotListener { snapshot, e ->
            val paintingsResponse = if (snapshot != null) {
                val paintings: List<Painting> = snapshot.toObjects<Painting>()
                Log.d(TAG, "getAllPaintings: data fetched from Firestore - ${paintings}") // And this
                Log.d(TAG, "getAllPaintings: ${paintings}")
                paintingsList.clear()
                paintingsList.addAll(paintings)
                Result.Success(paintings)
            } else {
                Result.Error(e ?: Exception("Unknown error occurred"))
            }
            trySend(paintingsResponse).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getPainting(id: String): Result<Painting> {
        val localPainting = paintingsList.find { it.id == id }
        if (localPainting != null) {
            Log.d(TAG, "getPainting: returned non-null result")
            return Result.Success(localPainting)
        }

        return try {
            val docSnapshot = paintingRef.document(id).get().await()
            if (docSnapshot != null) {
                val remotePainting = docSnapshot.toObject<Painting>()
                if (remotePainting != null) {
                    Result.Success(remotePainting)
                } else {
                    Result.Error(Exception("Document found, but could not convert to Painting"))
                }
            } else {
                Result.Error(Exception("No document found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
