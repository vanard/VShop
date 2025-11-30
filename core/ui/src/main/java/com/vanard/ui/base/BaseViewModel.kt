package com.vanard.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.common.helpers.AppException
import com.vanard.common.helpers.Event
import com.vanard.common.UIState
import com.vanard.common.helpers.ViewModelEvent
import com.vanard.common.helpers.requiresReauth
import com.vanard.common.helpers.toAppException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Enhanced base ViewModel with error handling, loading states, and event management
 */
open class BaseViewModel : ViewModel() {

    // UI State management
    private val _uiState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()

    // Event flow for one-time events (navigation, toasts, etc.)
    private val _events = MutableSharedFlow<Event<ViewModelEvent>>()
    val events: SharedFlow<Event<ViewModelEvent>> = _events.asSharedFlow()

    // Exception handler for safe coroutine execution
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    protected val safeScope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    // ========== State Management ==========

    protected fun setLoading() {
        _uiState.value = UIState.Loading
    }

    protected fun setError(message: String) {
        _uiState.value = UIState.Error(message)
        Log.e(TAG, "Error: $message")
    }

    protected fun setSuccess() {
        _uiState.value = UIState.Success(Unit)
    }

    protected fun setIdle() {
        _uiState.value = UIState.Idle
    }

    // ========== Error Handling ==========

    /**
     * Handle errors with automatic auth detection
     */
    protected open fun handleError(throwable: Throwable) {
        val appException = throwable.toAppException()

        Log.e(TAG, "Error occurred: ${appException.message}", appException)

        // Check if it's an auth error
        if (appException.requiresReauth()) {
            handleAuthError(appException as AppException.AuthException)
        } else {
            // Set error state
            setError(appException.message ?: "An error occurred")
        }
    }

    /**
     * Handle authentication errors
     * Subclasses can override for custom behavior
     */
    protected open fun handleAuthError(exception: AppException.AuthException) {
        Log.w(TAG, "Auth error: ${exception.message}")

        when (exception) {
            is AppException.AuthException.SessionExpired -> {
                emitEvent(ViewModelEvent.SessionEvent.SessionExpired)
            }

            is AppException.AuthException.Unauthorized,
            is AppException.AuthException.InvalidToken -> {
                emitEvent(ViewModelEvent.SessionEvent.Unauthorized)
            }

            else -> {
                setError(exception.message ?: "Authentication failed")
            }
        }
    }

    // ========== Event Management ==========

    /**
     * Emit a one-time event
     */
    protected fun emitEvent(event: ViewModelEvent) {
        viewModelScope.launch {
            _events.emit(Event(event))
        }
    }

    protected fun showToast(message: String) {
        emitEvent(ViewModelEvent.MessageEvent.ShowToast(message))
    }

    protected fun showSnackbar(message: String, action: String? = null) {
        emitEvent(ViewModelEvent.MessageEvent.ShowSnackbar(message, action))
    }

    protected fun showError(message: String) {
        emitEvent(ViewModelEvent.MessageEvent.ShowError(message))
    }

    protected fun navigateTo(route: String) {
        emitEvent(ViewModelEvent.NavigationEvent.NavigateTo(route))
    }

    protected fun navigateBack() {
        emitEvent(ViewModelEvent.NavigationEvent.NavigateBack)
    }

    // ========== Safe Execution Helpers ==========

    /**
     * Execute a suspending operation safely with loading state
     */
    protected suspend fun <T> executeWithLoading(
        updateState: Boolean = true,
        operation: suspend () -> T
    ): Result<T> {
        return try {
            if (updateState) setLoading()
            val result = operation()
            if (updateState) setSuccess()
            Result.success(result)
        } catch (e: Exception) {
            handleError(e)
            Result.failure(e)
        }
    }

    /**
     * Execute operation and map to UIState
     */
    protected suspend fun <T> executeToUIState(
        operation: suspend () -> T
    ): UIState<T> {
        return try {
            setLoading()
            val result = operation()
            UIState.Success(result)
        } catch (e: Exception) {
            val appException = e.toAppException()
            handleError(appException)
            UIState.Error(appException.message ?: "Operation failed")
        }
    }

    /**
     * Execute operation that requires authentication
     */
    protected suspend fun <T> executeAuthRequired(
        operation: suspend () -> T
    ): Result<T> {
        return try {
            setLoading()
            val result = operation()
            setSuccess()
            Result.success(result)
        } catch (e: Exception) {
            val appException = e.toAppException()
            if (appException.requiresReauth()) {
                handleAuthError(appException as AppException.AuthException)
            } else {
                handleError(appException)
            }
            Result.failure(appException)
        }
    }

    /**
     * Launch a safe coroutine with automatic error handling
     */
    protected fun launchSafe(
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(exceptionHandler) {
        block()
    }

    // ========== Utility Methods ==========

    /**
     * Reset the ViewModel state
     */
    protected open fun resetState() {
        _uiState.value = UIState.Idle
    }

    companion object {
        private const val TAG = "BaseViewModel"
    }
}