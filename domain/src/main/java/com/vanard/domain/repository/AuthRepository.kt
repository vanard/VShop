package com.vanard.domain.repository

import com.vanard.common.UIState
import com.vanard.domain.model.User
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.model.auth.LoginRequest
import com.vanard.domain.model.auth.SignUpRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Flow<UIState<AuthResponse>>
    suspend fun signUp(request: SignUpRequest): Flow<UIState<AuthResponse>>
    suspend fun logout()
    fun getCurrentUser(): Flow<UIState<User?>>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun saveUserSession(user: User, token: String)
    suspend fun clearUserSession()
}