package com.example.ziadartwork.di

import com.example.ziadartwork.data.repositoryImpl.PaintingsRepoImpl
import com.example.ziadartwork.domain.repository.PaintingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class PaintingsRepositoryModule {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    @Singleton
    abstract fun paintingRepoProvider(paintingsRepo: PaintingsRepoImpl) : PaintingsRepository
}
