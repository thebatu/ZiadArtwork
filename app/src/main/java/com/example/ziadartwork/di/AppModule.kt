package com.example.ziadartwork.di

import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.domain.repository.PaintingsRepository
import com.example.ziadartwork.domain.usecases.PaintingsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesPaintingsUserCases(repo: PaintingsRepository): PaintingsUseCases {
        return PaintingsUseCases(repo)
    }

    @Provides
    fun providesDispatchersIO(): AppDispatchers {
        return AppDispatchers()

    }

    @Provides
    @Singleton
    fun provideSnackbarManager(): SnackbarManager {
        return SnackbarManager
    }


}