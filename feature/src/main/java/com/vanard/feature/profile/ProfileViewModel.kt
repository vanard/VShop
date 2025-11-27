package com.vanard.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.common.UIState
import com.vanard.domain.model.User
import com.vanard.domain.usecase.AuthUseCase
import com.vanard.domain.usecase.auth.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<UIState<User?>>(UIState.Loading)
    val userState: StateFlow<UIState<User?>> = _userState.asStateFlow()

    private val _logoutState = MutableStateFlow<UIState<Unit>>(UIState.Loading)
    val logoutState: StateFlow<UIState<Unit>> = _logoutState.asStateFlow()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            userUseCase.getCurrentUser().collect { state ->
                _userState.value = state
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _logoutState.value = UIState.Loading
                authUseCase.logout()
                _logoutState.value = UIState.Success(Unit)
            } catch (e: Exception) {
                _logoutState.value = UIState.Error("Logout failed: ${e.message}")
            }
        }
    }

    fun refreshUser() {
        getCurrentUser()
    }

    companion object {
        const val TAG = "ProfileScreenViewModel"
    }
}