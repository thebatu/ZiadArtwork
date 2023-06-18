package com.example.ziadartwork.domain.repository

import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.ui.viewmodels.Result
import kotlinx.coroutines.flow.Flow


interface PaintingsRepository {
    fun getAllPaintings(): Flow<Result<List<Painting>>>
    suspend fun getPainting(id: String): Result<Painting>
}
