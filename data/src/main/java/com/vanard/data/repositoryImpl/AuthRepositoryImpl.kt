package com.vanard.data.repositoryImpl

import com.vanard.common.UIState
import com.vanard.common.helpers.UUIDv7
import com.vanard.data.dao.UserDao
import com.vanard.data.mappers.toDomain
import com.vanard.data.mappers.toEntity
import com.vanard.data.preferences.UserPreferencesManager
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.model.auth.LoginRequest
import com.vanard.domain.model.auth.SignUpRequest
import com.vanard.domain.model.User
import com.vanard.domain.model.dummyUser
import com.vanard.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val preferencesManager: UserPreferencesManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Flow<UIState<AuthResponse>> = flow {
        emit(UIState.Loading)
        try {
            // For demo purposes, we'll simulate authentication
            // In a real app, this would call a web service
            val user = userDao.getUserByEmail(request.email)

            if (user != null) {
                val token = generateAuthToken()
                val authResponse = AuthResponse(
                    user = user.toDomain(),
                    token = token
                )

                // Save user session
                preferencesManager.saveUserSession(
                    userId = user.id,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    phone = user.phone,
                    token = token
                )

                emit(UIState.Success(authResponse))
            } else {
                // For demo: check if it's demo credentials
                if (request.email == "demo@vshop.com" && request.password == "demo123") {
                    // Insert demo user
                    userDao.insertUser(dummyUser.toEntity().copy(id = dummyUser.id))

                    val token = generateAuthToken()
                    val authResponse = AuthResponse(user = dummyUser, token = token)

                    preferencesManager.saveUserSession(
                        userId = dummyUser.id,
                        email = dummyUser.email,
                        firstName = dummyUser.firstName,
                        lastName = dummyUser.lastName,
                        phone = dummyUser.phone,
                        token = token
                    )

                    emit(UIState.Success(authResponse))
                } else {
                    emit(UIState.Error("Invalid email or password"))
                }
            }
        } catch (e: Exception) {
            emit(UIState.Error("Login failed: ${e.message}"))
        }
    }

    override suspend fun signUp(request: SignUpRequest): Flow<UIState<AuthResponse>> = flow {
        emit(UIState.Loading)
        try {
            // Check if user already exists
            val existingUser = userDao.getUserByEmail(request.email)
            if (existingUser != null) {
                emit(UIState.Error("User with this email already exists"))
                return@flow
            }

            // Create new user
            val newUser = User(
                id = generateIDUser(),
                email = request.email,
                firstName = request.firstName,
                lastName = request.lastName,
                phone = request.phone
            )

            userDao.insertUser(newUser.toEntity())
            val token = generateAuthToken()

            val authResponse = AuthResponse(
                user = newUser,
                token = token
            )

            // Save user session
            preferencesManager.saveUserSession(
                userId = newUser.id,
                email = request.email,
                firstName = request.firstName,
                lastName = request.lastName,
                phone = request.phone,
                token = token
            )

            emit(UIState.Success(authResponse))
        } catch (e: Exception) {
            emit(UIState.Error("Sign up failed: ${e.message}"))
        }
    }

    override suspend fun logout() {
        preferencesManager.clearUserSession()
    }

    override fun getCurrentUser(): Flow<UIState<User?>> {
        // Combine all user preference flows to react to changes
        return combine(
            preferencesManager.isLoggedIn,
            preferencesManager.userPrefsFlow
        ) { isLoggedIn: Boolean, user ->
            try {
                if (isLoggedIn && user.id.isNotEmpty()) {
                    UIState.Success(user)
                } else {
                    UIState.Success(null)
                }
            } catch (e: Exception) {
                UIState.Error("Failed to get current user: ${e.message}")
            }
        }
    }

//    override suspend fun getCurrentUser(): Flow<UIState<User?>> = flow {
//        try {
//            val isLoggedIn = preferencesManager.isLoggedIn.first()
//            if (isLoggedIn) {
//                val userId = preferencesManager.userId.first()
//                val email = preferencesManager.userEmail.first()
//                val firstName = preferencesManager.userFirstName.first()
//                val lastName = preferencesManager.userLastName.first()
//                val phone = preferencesManager.userPhone.first()
//
//                if (userId > 0) {
//                    val user = User(
//                        id = userId,
//                        email = email,
//                        firstName = firstName,
//                        lastName = lastName,
//                        phone = phone.takeIf { it.isNotEmpty() }
//                    )
//                    emit(UIState.Success(user))
//                } else {
//                    emit(UIState.Success(null))
//                }
//            } else {
//                emit(UIState.Success(null))
//            }
//        } catch (e: Exception) {
//            emit(UIState.Error("Failed to get current user: ${e.message}"))
//        }
//    }

    override suspend fun isUserLoggedIn(): Boolean {
        return preferencesManager.isLoggedIn.first() && preferencesManager.userId.first().isNotEmpty()
    }

    override suspend fun saveUserSession(user: User, token: String) {
        preferencesManager.saveUserSession(
            userId = user.id,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            phone = user.phone,
            token = token
        )
    }

    override suspend fun clearUserSession() {
        preferencesManager.clearUserSession()
    }

    private fun generateAuthToken(): String {
        return "token_${UUID.randomUUID()}"
    }

    private fun generateIDUser(): String {
        return UUIDv7.randomUUID().toString()
    }
}