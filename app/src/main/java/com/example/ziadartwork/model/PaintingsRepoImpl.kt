package com.example.ziadartwork.model

import android.util.Log
import com.example.ziadartwork.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class PaintingsRepoImpl @Inject constructor() : PaintingsRepository {
    private val paintingRef: CollectionReference = Firebase.firestore.collection("paintings")

    companion object {
        public val TAG = PaintingsRepoImpl::class.simpleName

    }

    private var paintingsList: MutableList<Painting> = mutableListOf<Painting>()

    override fun getAllPaintings(): Flow<Result<List<Painting>>> = callbackFlow {

        val snapshotListener = paintingRef.addSnapshotListener { snapshot, e ->
            val paintingsResponse = if (snapshot != null) {
                val paintings: List<Painting> = snapshot.toObjects<Painting>()
                Log.d(TAG, "getAllPaintings: ${paintings.toString()}")
                paintingsList.clear()
                paintingsList.addAll(paintings)
                Result.Success(paintings)
            } else {
                Result.Error(e)
            }
            trySend(paintingsResponse).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getPainting(id: String): Result<Painting> {
        val painting = paintingsList.find { it.id == id }
        if (painting != null) {
            Log.d(TAG, "getPainting: returned non-null result")
            return Result.Success(painting)
        }
        return getPaintingFromNetwork(id).single()
    }

    private fun getPaintingFromNetwork(id: String): Flow<Result<Painting>> = callbackFlow {

        val docRef = paintingRef.document(id)
        val listenerRegistration = docRef.addSnapshotListener { snapshot, e ->
            val painting = snapshot?.toObject<Painting>()
            val paintingResponse = if (painting != null) {
                Result.Success(painting)
            } else {
                Result.Error(e ?: Exception("Painting not found"))
            }
            trySend(paintingResponse)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }
}
