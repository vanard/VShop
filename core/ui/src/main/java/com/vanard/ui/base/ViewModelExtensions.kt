package com.vanard.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.common.helpers.Event
import com.vanard.common.Result
import com.vanard.common.UIState
import com.vanard.common.helpers.ViewModelEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Extension functions for ViewModels
 */

// ========== State Management Extensions ==========

/**
 * Create a mutable state flow with initial value
 */
fun <T> ViewModel.mutableStateFlowOf(initial: T): MutableStateFlow<T> = MutableStateFlow(initial)

/**
 * Update state flow value safely
 */
fun <T> MutableStateFlow<T>.update(transform: (T) -> T) {
    value = transform(value)
}

/**
 * Emit a value to a flow in viewModelScope
 */
fun <T> ViewModel.emitTo(flow: MutableStateFlow<T>, value: T) {
    viewModelScope.launch {
        flow.emit(value)
    }
}

// ========== Flow to UIState Conversions ==========

/**
 * Map a Flow<Result<T>> to MutableStateFlow<UIState<T>>
 */
fun <T> ViewModel.collectResultAsUIState(
    resultFlow: Flow<Result<T>>,
    stateFlow: MutableStateFlow<UIState<T>>
) {
    viewModelScope.launch {
        resultFlow.collect { result ->
            stateFlow.value = when (result) {
                is Result.Success -> UIState.Success(result.data)
                is Result.Error -> UIState.Error(result.exception.message ?: "Error occurred")
                is Result.Loading -> UIState.Loading
            }
        }
    }
}

/**
 * Map a Flow<T> to MutableStateFlow<UIState<T>> with loading and error handling
 */
fun <T> ViewModel.collectAsUIState(
    flow: Flow<T>,
    stateFlow: MutableStateFlow<UIState<T>>,
    onError: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        stateFlow.value = UIState.Loading
        try {
            flow.collect { data ->
                stateFlow.value = UIState.Success(data)
            }
        } catch (e: Exception) {
            stateFlow.value = UIState.Error(e.message ?: "Error occurred")
            onError(e)
        }
    }
}

// ========== Composable Extensions ==========

/**
 * Observe events from BaseViewModel and handle them
 */
@Composable
fun BaseViewModel.ObserveEvents(
    onEvent: (ViewModelEvent) -> Unit
) {
    val events by this.events.collectAsState(initial = Event(ViewModelEvent.MessageEvent.ShowToast("")))

    LaunchedEffect(events) {
        events.getContentIfNotHandled()?.let { event ->
            onEvent(event)
        }
    }
}

/**
 * Observe session events from SessionViewModel
 */
@Composable
fun SessionViewModel.ObserveSessionEvents(
    onSessionExpired: () -> Unit = {},
    onUnauthorized: () -> Unit = {},
    onLoggedOut: () -> Unit = {}
) {
    ObserveEvents { event ->
        when (event) {
            is ViewModelEvent.SessionEvent.SessionExpired -> onSessionExpired()
            is ViewModelEvent.SessionEvent.Unauthorized -> onUnauthorized()
            is ViewModelEvent.SessionEvent.LoggedOut -> onLoggedOut()
            else -> { /* Ignore other events */
            }
        }
    }
}

// ========== Helper Functions ==========

/**
 * Check if UIState is successful and has data
 */
fun <T> UIState<T>.isSuccessWithData(): Boolean {
    return this is UIState.Success && this.data != null
}

/**
 * Get data from UIState if successful, null otherwise
 */
fun <T> UIState<T>.getDataOrNull(): T? {
    return if (this is UIState.Success) this.data else null
}

/**
 * Get error message from UIState if error, null otherwise
 */
fun <T> UIState<T>.getErrorOrNull(): String? {
    return if (this is UIState.Error) this.errorMessage else null
}

/**
 * Execute block only if UIState is success
 */
inline fun <T> UIState<T>.onSuccess(block: (T) -> Unit) {
    if (this is UIState.Success) {
        block(this.data)
    }
}

/**
 * Execute block only if UIState is error
 */
inline fun <T> UIState<T>.onError(block: (String) -> Unit) {
    if (this is UIState.Error) {
        block(this.errorMessage)
    }
}

/**
 * Execute block only if UIState is loading
 */
inline fun <T> UIState<T>.onLoading(block: () -> Unit) {
    if (this is UIState.Loading) {
        block()
    }
}

/**
 * Check if StateFlow has loaded data successfully
 */
fun <T> StateFlow<UIState<T>>.hasData(): Boolean {
    return value is UIState.Success
}

/**
 * Get current data from StateFlow or null
 */
fun <T> StateFlow<UIState<T>>.currentData(): T? {
    return (value as? UIState.Success)?.data
}
