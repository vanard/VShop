package com.vanard.domain.usecase.auth

import com.vanard.common.UIState
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.model.auth.LoginRequest
import com.vanard.domain.repository.AuthRepository
import com.vanard.domain.validation.AuthValidation
import com.vanard.domain.validation.getErrorMessage
import com.vanard.domain.validation.isValid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<UIState<AuthResponse>> =
        flow {
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
                    // Validation passed, proceed with authentication
                    authRepository.login(LoginRequest(email, password)).collect { result ->
                        emit(result)
                    }
                }
            }
    }
}