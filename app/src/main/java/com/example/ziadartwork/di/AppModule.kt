package com.example.ziadartwork.di

import com.example.ziadartwork.AppDispatchers
import com.example.ziadartwork.model.PaintingsRepository
import com.example.ziadartwork.usecases.PaintingsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun providesCartsRepository(): CartRepository {

    }

    @Provides
    @Singleton
    fun providesPaintingsUserCases(repo: PaintingsRepository): PaintingsUseCases {
        return PaintingsUseCases(repo)
    }

}