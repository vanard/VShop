package com.vanard.data.repositoryImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vanard.common.UIState
import com.vanard.data.mappers.toAuthResponse
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.model.auth.LoginRequest
import com.vanard.domain.model.auth.SignUpRequest
import com.vanard.domain.model.User
import com.vanard.domain.repository.AuthRepository
import com.vanard.data.preferences.SecureUserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val securePrefs: SecureUserPreferencesManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Flow<UIState<AuthResponse>> = flow {
        emit(UIState.Loading)
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(request.email, request.password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val authResponse = firebaseUser.toAuthResponse()
                // Save token securely
                securePrefs.saveAuthToken(firebaseUser.getIdToken(false).await().token!!)
                emit(UIState.Success(authResponse))
            } else {
                emit(UIState.Error("Login failed: unknown error"))
            }
        } catch (e: Exception) {
            emit(UIState.Error("Login failed: ${e.message}"))
        }
    }

    override suspend fun signUp(request: SignUpRequest): Flow<UIState<AuthResponse>> = flow {
        emit(UIState.Loading)
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(request.email, request.password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val authResponse = firebaseUser.toAuthResponse()
                securePrefs.saveAuthToken(firebaseUser.getIdToken(false).await().token!!)
                emit(UIState.Success(authResponse))
            } else {
                emit(UIState.Error("Sign up failed: unknown error"))
            }
        } catch (e: Exception) {
            emit(UIState.Error("Sign up failed: ${e.message}"))
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        securePrefs.clearAuthToken()
    }

    override fun getCurrentUser(): Flow<UIState<User?>> {
        // Not using local DataStore for user details; return empty until you sync.
        return flow { emit(UIState.Success(null)) }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun saveUserSession(user: User, token: String) {
        // Store token securely; other user details can be persisted elsewhere if needed.
        securePrefs.saveAuthToken(token)
    }

    override suspend fun clearUserSession() {
        securePrefs.clearAuthToken()
    }
}
