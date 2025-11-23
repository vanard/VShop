package com.vanard.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.common.UIState
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.usecase.AuthUseCase
import com.vanard.domain.usecase.auth.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<UIState<AuthResponse>>(UIState.Idle)
    val loginState: StateFlow<UIState<AuthResponse>> = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow<UIState<AuthResponse>>(UIState.Idle)
    val signUpState: StateFlow<UIState<AuthResponse>> = _signUpState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.login(email, password).collect { state ->
                _loginState.value = state
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String? = null
    ) {
        viewModelScope.launch {
            authUseCase.signUp(email, password, firstName, lastName, phone).collect { state ->
                _signUpState.value = state
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
        }
    }

    fun validateEmail(email: String): Boolean {
        return authUseCase.validateEmail(email)
    }

    fun validatePassword(password: String): Boolean {
        return authUseCase.validatePassword(password)
    }

    fun getEmailValidationError(email: String): String? {
        return authUseCase.getEmailValidationError(email)
    }

    fun getPasswordValidationError(password: String): String? {
        return authUseCase.getPasswordValidationError(password)
    }

    fun resetLoginState() {
        _loginState.value = UIState.Idle
    }

    fun resetSignUpState() {
        _signUpState.value = UIState.Idle
    }
}