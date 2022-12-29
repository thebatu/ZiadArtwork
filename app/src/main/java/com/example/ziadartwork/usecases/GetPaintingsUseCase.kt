package com.example.ziadartwork.usecases

import com.example.ziadartwork.model.PaintingsRepository

class GetPaintingsUseCase(private val repo: PaintingsRepository) {
    operator fun invoke() = repo.getAllPaintings()
}
