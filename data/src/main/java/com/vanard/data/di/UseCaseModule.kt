package com.vanard.data.di

import com.vanard.domain.repository.AuthRepository
import com.vanard.domain.repository.CartRepository
import com.vanard.domain.repository.ProductRepository
import com.vanard.domain.usecase.AuthUseCase
import com.vanard.domain.usecase.cart.CartUseCase
import com.vanard.domain.usecase.auth.UserUseCase
import com.vanard.domain.usecase.product.ProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    /**
     * Provides AuthUseCase for authentication operations
     * Includes: login, signup, logout, session validation
     */
    @Provides
    @Singleton
    fun provideAuthUseCase(
        authRepository: AuthRepository
    ): AuthUseCase {
        return AuthUseCase(authRepository)
    }

    /**
     * Provides UserUseCase for user data operations
     * Includes: getCurrentUser, user profile, user preferences
     */
    @Provides
    @Singleton
    fun provideUserUseCase(
        authRepository: AuthRepository
    ): UserUseCase {
        return UserUseCase(authRepository)
    }

    /**
     * Provides consolidated ProductUseCase for all product operations
     * Includes: getAllProducts, getProductById, getFavoriteProducts,
     * updateProduct, cart operations, etc.
     */
    @Provides
    @Singleton
    fun provideProductUseCase(
        productRepository: ProductRepository
    ): ProductUseCase {
        return ProductUseCase(productRepository)
    }

    /**
     * Provides CartUseCase for cart-specific operations
     */
    @Provides
    @Singleton
    fun provideCartUseCase(
        cartRepository: CartRepository
    ): CartUseCase {
        return CartUseCase(cartRepository)
    }
}