package com.vanard.data.di

import com.vanard.data.remote.ApiService
import com.vanard.data.repositoryImpl.ProductRepositoryImpl
import com.vanard.domain.repository.ProductRepository
import com.vanard.domain.usecase.HomeUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
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