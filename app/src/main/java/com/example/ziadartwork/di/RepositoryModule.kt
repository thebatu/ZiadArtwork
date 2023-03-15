package com.example.ziadartwork.di

import com.example.ziadartwork.model.PaintingsRepoImpl
import com.example.ziadartwork.model.PaintingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun paintingRepoProvider(paintingsRepo: PaintingsRepoImpl) : PaintingsRepository
}
