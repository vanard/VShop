package com.vanard.vshop.di

import android.content.Context
import androidx.room.Room
import com.vanard.data.dao.ProductDao
import com.vanard.data.db.AppDatabase
import com.vanard.data.di.DatabaseModule
import com.vanard.data.di.RepositoryModule
import com.vanard.domain.repository.ProductRepository
import com.vanard.vshop.repository.FakeProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class, RepositoryModule::class]
)
object TestDI {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.getProductDao()

    @Provides
    fun provideRepoImpl(productDao: ProductDao): ProductRepository {
        return FakeProductRepository(productDao)
    }
}