package com.vanard.vshop.di

import com.vanard.vshop.data.remote.ApiService
import com.vanard.vshop.data.repositoryImpl.ProductRepositoryImpl
import com.vanard.vshop.domain.HomeUseCase
import com.vanard.vshop.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class RepositoryModule {
//
//    @Singleton
//    @Binds
//    abstract fun bindProductRepository(
//        productRepositoryImpl: ProductRepositoryImpl
//    ): ProductRepository
//
//}

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideProductRepository(
        api: ApiService
    ): ProductRepository {
        return ProductRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideHomeUseCase(productRepository: ProductRepository): HomeUseCase {
        return HomeUseCase(productRepository)
    }

}