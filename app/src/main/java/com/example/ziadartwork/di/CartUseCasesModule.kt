package com.example.ziadartwork.di

import com.example.ziadartwork.domain.usecases.CartUseCases
import com.example.ziadartwork.data.repository.CartUseCasesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CartUseCasesModule {

    @Binds
    @Singleton
    abstract fun provideCartUsesImpl(
        cartUsesCases: CartUseCasesImpl
    ): CartUseCases

}