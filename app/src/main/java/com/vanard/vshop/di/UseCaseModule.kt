package com.vanard.vshop.di

import com.vanard.domain.repository.CartRepository
import com.vanard.domain.repository.ProductRepository
import com.vanard.domain.usecase.CartUseCase
import com.vanard.domain.usecase.HomeUseCase
import com.vanard.domain.usecase.WishlistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideHomeUseCase(
        repository: ProductRepository
    ): HomeUseCase {
        return HomeUseCase(repository)
    }

    @Provides
    fun provideCartUseCase(
        repository: CartRepository
    ): CartUseCase {
        return CartUseCase(repository)
    }

    @Provides
    fun provideWhishListUseCase(
        repository: ProductRepository
    ): WishlistUseCase {
        return WishlistUseCase(repository)
    }

}