package com.vanard.data.di

import android.content.Context
import androidx.room.Room
import com.vanard.data.dao.CartDao
import com.vanard.data.dao.ProductDao
import com.vanard.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shop_database.db"
        ).build()
    }

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.ProductDao()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.CartDao()
}