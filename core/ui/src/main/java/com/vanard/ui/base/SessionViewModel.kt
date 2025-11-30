package com.vanard.ui.base

import android.util.Log
import com.vanard.common.helpers.AppException
import com.vanard.common.UIState
import com.vanard.common.helpers.ViewModelEvent
import com.vanard.common.helpers.requiresReauth
import com.vanard.common.helpers.toAppException
import com.vanard.domain.model.User
import com.vanard.domain.usecase.AuthUseCase
import com.vanard.domain.usecase.auth.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Enhanced SessionViewModel that extends BaseViewModel for better error handling
 * and event management
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userUseCase: UserUseCase,    // For user data operations
    private val authUseCase: AuthUseCase     // For authentication checks and logout
) : BaseViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _userState = MutableStateFlow<UIState<User?>>(UIState.Loading)
    val userState: StateFlow<UIState<User?>> = _userState.asStateFlow()

    private var userCollectionJob: Job? = null

    init {
        checkSession()
    }

    // ========== Session Management ==========

    /**
     * Check current session status
     */
    fun checkSession() {
        launchSafe {
            try {
                _sessionState.value = SessionState.Loading

                val isLoggedIn = authUseCase.isUserLoggedIn()

                if (isLoggedIn) {
                    _sessionState.value = SessionState.Authenticated
                    getCurrentUser()
                } else {
                    _sessionState.value = SessionState.NotAuthenticated
                    _userState.value = UIState.Success(null)
                }
            } catch (e: Exception) {
                val appException = e.toAppException()
                Log.e(TAG, "Failed to check session", appException)

                _sessionState.value = SessionState.Error(
                    appException.message ?: "Failed to check session"
                )
                _userState.value = UIState.Error(
                    appException.message ?: "Failed to check session"
                )
            }
        }
    }

    /**
     * Get current user data
     */
    private fun getCurrentUser() {
        // Cancel previous collection if any
        userCollectionJob?.cancel()

        userCollectionJob = launchSafe {
            try {
                userUseCase.getCurrentUser().collect { result ->
                    _userState.value = result

                    // Update session state based on user data
                    when (result) {
                        is UIState.Success -> {
                            val userData = result.data
                            if (userData != null) {
                                _sessionState.value = SessionState.Authenticated
                                Log.d(TAG, "User authenticated: ${userData.email}")
                            } else {
                                _sessionState.value = SessionState.NotAuthenticated
                                Log.d(TAG, "No user data available")
                            }
                        }

                        is UIState.Error -> {
                            // Check if it's an auth error
                            val errorMessage = result.errorMessage
                            if (errorMessage.contains("401") ||
                                errorMessage.contains("unauthorized", ignoreCase = true) ||
                                errorMessage.contains("expired", ignoreCase = true)
                            ) {
                                _sessionState.value = SessionState.SessionExpired
                                emitEvent(ViewModelEvent.SessionEvent.SessionExpired)
                            } else {
                                _sessionState.value = SessionState.Error(errorMessage)
                            }
                            Log.e(TAG, "Failed to get user: $errorMessage")
                        }

                        is UIState.Loading -> {
                            // Keep current authentication state while loading
                        }

                        is UIState.Idle -> {
                            // Initial state
                        }
                    }
                }
            } catch (e: Exception) {
                val appException = e.toAppException()
                Log.e(TAG, "Error collecting user data", appException)

                if (appException.requiresReauth()) {
                    _sessionState.value = SessionState.SessionExpired
                    emitEvent(ViewModelEvent.SessionEvent.SessionExpired)
                } else {
                    _sessionState.value = SessionState.Error(
                        appException.message ?: "Failed to get user"
                    )
                }
                _userState.value = UIState.Error(
                    appException.message ?: "Failed to get user"
                )
            }
        }
    }

    /**
     * Refresh session and user data
     */
    fun refreshSession() {
        Log.d(TAG, "Refreshing session")
        checkSession()
    }

    /**
     * Update user data without full session check
     * Useful for profile updates
     */
    fun refreshUserData() {
        launchSafe {
            val currentState = _sessionState.value
            if (currentState is SessionState.Authenticated) {
                getCurrentUser()
            } else {
                Log.w(TAG, "Cannot refresh user data - not authenticated")
            }
        }
    }

    // ========== Authentication Actions ==========

    /**
     * Logout user and clear session
     */
    fun logout() {
        launchSafe {
            try {
                Log.d(TAG, "Logging out user")
                setLoading()

                authUseCase.logout()

                _sessionState.value = SessionState.NotAuthenticated
                _userState.value = UIState.Success(null)

                emitEvent(ViewModelEvent.SessionEvent.LoggedOut)
                setSuccess()

                Log.d(TAG, "Logout successful")
            } catch (e: Exception) {
                val appException = e.toAppException()
                Log.e(TAG, "Logout failed", appException)

                _sessionState.value = SessionState.Error(
                    appException.message ?: "Logout failed"
                )
                setError(appException.message ?: "Logout failed")
            }
        }
    }

    /**
     * Mark session as expired (called from other ViewModels or interceptors)
     */
    fun markSessionExpired() {
        Log.w(TAG, "Session marked as expired")
        _sessionState.value = SessionState.SessionExpired
        _userState.value = UIState.Success(null)
        emitEvent(ViewModelEvent.SessionEvent.SessionExpired)
    }

    /**
     * Mark as unauthorized (called from other ViewModels)
     */
    fun markUnauthorized() {
        Log.w(TAG, "Marked as unauthorized")
        _sessionState.value = SessionState.NotAuthenticated
        _userState.value = UIState.Success(null)
        emitEvent(ViewModelEvent.SessionEvent.Unauthorized)
    }

    // ========== Query Methods ==========

    /**
     * Get current user synchronously (if available)
     */
    fun getCurrentUserSync(): User? {
        return when (val state = _userState.value) {
            is UIState.Success -> state.data
            else -> null
        }
    }

    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return _sessionState.value is SessionState.Authenticated
    }

    /**
     * Check if session is loading
     */
    fun isSessionLoading(): Boolean {
        return _sessionState.value is SessionState.Loading
    }

    /**
     * Get current user email if available
     */
    fun getCurrentUserEmail(): String? {
        return getCurrentUserSync()?.email
    }

    /**
     * Get current user ID if available
     */
    fun getCurrentUserId(): String? {
        return getCurrentUserSync()?.id
    }

    // ========== Override Error Handling ==========

    override fun handleAuthError(exception: AppException.AuthException) {
        Log.w(TAG, "Auth error in SessionViewModel: ${exception.message}")

        when (exception) {
            is AppException.AuthException.SessionExpired -> {
                markSessionExpired()
            }

            is AppException.AuthException.Unauthorized,
            is AppException.AuthException.InvalidToken -> {
                markUnauthorized()
            }

            else -> {
                _sessionState.value = SessionState.Error(
                    exception.message ?: "Authentication error"
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userCollectionJob?.cancel()
        Log.d(TAG, "SessionViewModel cleared")
    }

    companion object {
        private const val TAG = "SessionViewModel"
    }
}