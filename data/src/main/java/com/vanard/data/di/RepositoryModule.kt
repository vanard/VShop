package com.vanard.data.di

import com.vanard.data.repositoryImpl.CartRepositoryImpl
import com.vanard.data.repositoryImpl.ProductRepositoryImpl
import com.vanard.domain.repository.CartRepository
import com.vanard.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Singleton
    @Binds
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

}

//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryObjectModule {
//
//    @Singleton
//    @Provides
//    fun provideProductRepository(
//        api: ApiService
//    ): ProductRepository {
//        return ProductRepositoryImpl(api)
//    }

//    @Provides
//    @Singleton
//    fun provideHomeUseCase(productRepository: ProductRepository): HomeUseCase {
//        return HomeUseCase(productRepository)
//    }

//}