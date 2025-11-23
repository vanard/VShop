package com.vanard.data.di

import android.content.Context
import com.vanard.data.dao.UserDao
import com.vanard.data.db.AppDatabase
import com.vanard.data.preferences.UserPreferencesManager
import com.vanard.data.repositoryImpl.AuthRepositoryImpl
import com.vanard.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.getUserDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesManager(@ApplicationContext context: Context): UserPreferencesManager {
        return UserPreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        preferencesManager: UserPreferencesManager
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, preferencesManager)
    }
}