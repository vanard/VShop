package com.vanard.vshop.di

import com.vanard.domain.repository.ProductRepository
import com.vanard.domain.usecase.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideHomesUseCase(
        repository: ProductRepository
    ): HomeUseCase {
        return HomeUseCase(repository)
    }

}