package com.example.ziadartwork.di


import com.example.ziadartwork.domain.repository.CartRepository
import com.example.ziadartwork.data.repository.CartRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCartPreferencesRepository(
        cartRepository: CartRepositoryImpl
    ): CartRepository
}
