package com.example.ziadartwork.model

import com.example.ziadartwork.Response
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class PaintingsRepoImpl(
    private val paintingRef: CollectionReference

) : PaintingsRepository {
        override fun getAllPaintings(): Flow<Response<List<Painting>>> = callbackFlow {

        val snapshotListener = paintingRef.addSnapshotListener { snapshot, e ->
            val paintingsResponse = if (snapshot != null) {
                val paintings = snapshot.toObjects<Painting>()
                Response.Success(paintings)
            } else {
                Response.Failure(e)
            }
            trySend(paintingsResponse).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

}