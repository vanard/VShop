package com.vanard.domain.usecase

import com.vanard.common.UIState
import com.vanard.domain.model.User
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.model.auth.LoginRequest
import com.vanard.domain.model.auth.SignUpRequest
import com.vanard.domain.repository.AuthRepository
import com.vanard.domain.validation.AuthValidation
import com.vanard.domain.validation.getErrorMessage
import com.vanard.domain.validation.isValid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(email: String, password: String): Flow<UIState<AuthResponse>> = flow {
        // Validate input
        val emailValidation = AuthValidation.validateEmail(email)
        val passwordValidation = AuthValidation.validatePassword(password)

        when {
            !emailValidation.isValid() -> {
                emit(UIState.Error(emailValidation.getErrorMessage() ?: "Invalid email"))
                return@flow
            }

            !passwordValidation.isValid() -> {
                emit(UIState.Error(passwordValidation.getErrorMessage() ?: "Invalid password"))
                return@flow
            }

            else -> {
                authRepository.login(LoginRequest(email, password)).collect { result ->
                    emit(result)
                }
            }
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String? = null
    ): Flow<UIState<AuthResponse>> = flow {
        // Validate all inputs
        val emailValidation = AuthValidation.validateEmail(email)
        val passwordValidation = AuthValidation.validatePassword(password)
        val firstNameValidation = AuthValidation.validateName(firstName, "First name")
        val lastNameValidation = AuthValidation.validateName(lastName, "Last name")
        val phoneValidation = AuthValidation.validatePhone(phone)

        when {
            !emailValidation.isValid() -> {
                emit(UIState.Error(emailValidation.getErrorMessage() ?: "Invalid email"))
                return@flow
            }

            !passwordValidation.isValid() -> {
                emit(UIState.Error(passwordValidation.getErrorMessage() ?: "Invalid password"))
                return@flow
            }

            !firstNameValidation.isValid() -> {
                emit(UIState.Error(firstNameValidation.getErrorMessage() ?: "Invalid first name"))
                return@flow
            }

            !lastNameValidation.isValid() -> {
                emit(UIState.Error(lastNameValidation.getErrorMessage() ?: "Invalid last name"))
                return@flow
            }

            !phoneValidation.isValid() -> {
                emit(UIState.Error(phoneValidation.getErrorMessage() ?: "Invalid phone"))
                return@flow
            }

            else -> {
                authRepository.signUp(
                    SignUpRequest(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone
                    )
                ).collect { result ->
                    emit(result)
                }
            }
        }
    }

    suspend fun logout() {
        authRepository.logout()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    suspend fun saveUserSession(user: User, token: String) {
        authRepository.saveUserSession(user, token)
    }

    suspend fun clearUserSession() {
        authRepository.clearUserSession()
    }

    fun validateEmail(email: String): Boolean {
        return AuthValidation.validateEmail(email).isValid()
    }

    fun validatePassword(password: String): Boolean {
        return AuthValidation.validatePassword(password).isValid()
    }

    fun getEmailValidationError(email: String): String? {
        return AuthValidation.validateEmail(email).getErrorMessage()
    }

    fun getPasswordValidationError(password: String): String? {
        return AuthValidation.validatePassword(password).getErrorMessage()
    }
}