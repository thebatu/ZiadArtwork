package com.example.ziadartwork.DI

import com.example.ziadartwork.AppDispatchers
import com.example.ziadartwork.model.PaintingsRepoImpl
import com.example.ziadartwork.model.PaintingsRepository
import com.example.ziadartwork.usecases.GetPaintingsUseCase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val paintingRef: CollectionReference = Firebase.firestore.collection("paintings")

    @Provides
    @Singleton
    fun providePaintingsRepository(): PaintingsRepository {
        return PaintingsRepoImpl(paintingRef)
    }

    @Provides
    @Singleton
    fun providesPaintingsUserCases() : GetPaintingsUseCase {
        return GetPaintingsUseCase(providePaintingsRepository())

    }

    @Provides
    fun providesDispatchersIO(): AppDispatchers {
        return AppDispatchers()

    }



}