package com.example.ziadartwork.usecases

import com.example.ziadartwork.Result
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.model.PaintingsRepository
import kotlinx.coroutines.flow.Flow

class PaintingsUseCases(private val repo: PaintingsRepository) {

    operator fun invoke(): Flow<Result<List<Painting>>> = repo.getAllPaintings()
    suspend fun getPainting(id: String): Result<Painting> = repo.getPainting(id)

}
