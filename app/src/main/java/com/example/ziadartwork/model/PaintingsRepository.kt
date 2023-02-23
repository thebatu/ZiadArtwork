package com.example.ziadartwork.model

import com.example.ziadartwork.Result
import kotlinx.coroutines.flow.Flow


interface PaintingsRepository {
    fun getAllPaintings(): Flow<Result<List<Painting>>>
    suspend fun getPainting(id: String): Result<Painting>
}