package com.vanard.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.vanard.data.preferences.EncryptionManager
import com.vanard.data.preferences.SecureUserPreferencesManager
import com.vanard.data.preferences.UserPreferencesManager
import com.vanard.data.repositoryImpl.FirebaseAuthRepositoryImpl
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
    fun provideUserPreferencesManager(@ApplicationContext context: Context): UserPreferencesManager {
        return UserPreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideEncryptionManager(@ApplicationContext context: Context): EncryptionManager {
        return EncryptionManager(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSecureUserPreferencesManager(
        @ApplicationContext context: Context,
        encryptionManager: EncryptionManager
    ): SecureUserPreferencesManager {
        return SecureUserPreferencesManager(context, encryptionManager)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        securePrefs: SecureUserPreferencesManager
    ): AuthRepository {
        return FirebaseAuthRepositoryImpl(firebaseAuth, securePrefs)
    }
}