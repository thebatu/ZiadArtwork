package com.example.ziadartwork.data.repositoryImpl

import android.util.Log
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.repository.PaintingsRepository
import com.example.ziadartwork.ui.viewmodels.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
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

        val snapshotListener = paintingRef.addSnapshotListener { snapshot, e ->
            val paintingsResponse = if (snapshot != null) {
                val paintings: List<Painting> = snapshot.toObjects<Painting>()
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
        val painting = paintingsList.find { it.id == id }
        if (painting != null) {
            Log.d(TAG, "getPainting: returned non-null result")
            return Result.Success(painting)
        }

        return try {
            val docSnapshot = paintingRef.document(id).get().await()
            if (docSnapshot != null) {
                val painting = docSnapshot.toObject<Painting>()
                if (painting != null) {
                    Result.Success(painting)
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
