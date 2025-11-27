package com.vanard.ui.base

/**
 * Represents different session states in the application
 */
sealed class SessionState {
    /**
     * User is authenticated and session is valid
     */
    object Authenticated : SessionState()

    /**
     * User is not authenticated (no session)
     */
    object NotAuthenticated : SessionState()

    /**
     * User session has expired
     */
    object SessionExpired : SessionState()

    /**
     * Checking session status
     */
    object Loading : SessionState()

    /**
     * Error occurred while checking session
     */
    data class Error(val message: String) : SessionState()
}