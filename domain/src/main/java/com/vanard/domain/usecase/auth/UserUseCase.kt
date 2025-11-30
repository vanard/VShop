package com.vanard.domain.usecase.auth

import com.vanard.common.UIState
import com.vanard.domain.model.User
import com.vanard.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class UserUseCase(
    private val authRepository: AuthRepository
) {
    fun getCurrentUser(): Flow<UIState<User?>> {
        return authRepository.getCurrentUser()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    suspend fun updateUserProfile(user: User) {
        // In a real app, this might call userRepository.updateUser(user)
        // For now, we can save the updated user session
        authRepository.saveUserSession(user, "existing_token")
    }

    suspend fun getUserPreferences(): Map<String, Any> {
        // Example of user-specific operations
        // In a real app, this might call userRepository.getPreferences()
        return mapOf(
            "notifications_enabled" to true,
            "theme" to "light",
            "language" to "en"
        )
    }

    suspend fun updateUserPreferences(preferences: Map<String, Any>) {
        // In a real app, this might call userRepository.updatePreferences()
        // For now, this is just a placeholder for user data operations
    }

    fun getUserProfileCompleteness(user: User?): Int {
        if (user == null) return 0

        var completeness = 0

        // Basic info
        if (user.firstName.isNotEmpty()) completeness += 25
        if (user.lastName.isNotEmpty()) completeness += 25
        if (user.email.isNotEmpty()) completeness += 25

        // Optional info
        if (!user.phone.isNullOrEmpty()) completeness += 15
        if (user.address != null) completeness += 10

        return completeness
    }

    fun hasCompleteProfile(user: User?): Boolean {
        return getUserProfileCompleteness(user) >= 75
    }
}