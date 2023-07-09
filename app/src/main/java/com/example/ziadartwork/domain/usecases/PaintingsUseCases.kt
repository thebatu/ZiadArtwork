package com.example.ziadartwork.domain.usecases

import android.util.Log
import com.example.ziadartwork.ui.Result
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.repository.PaintingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "PaintingsUseCases"


class PaintingsUseCases @Inject constructor(private val repo: PaintingsRepository) {

    fun getAllPaintings(): Flow<Result<List<Painting>>> {
        Log.d(TAG, "getAllPaintings() in PaintingsUseCases called.")
        return repo.getAllPaintings()
    }
    suspend fun getPainting(id: String): Result<Painting> = repo.getPainting(id)

}
