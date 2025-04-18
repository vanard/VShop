package com.vanard.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.common.UIState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Unit>>(UIState.Loading)
    val uiState: StateFlow<UIState<Unit>> = _uiState

    protected var isLoaded = false

    protected fun setLoading() {
        _uiState.value = UIState.Loading
    }

    protected fun setError(message: String) {
        _uiState.value = UIState.Error(message)
    }

    protected fun setSuccess() {
        _uiState.value = UIState.Success(Unit)
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Handle your error here (e.g. show error state)
        Log.e("ViewModel", "Error: ${throwable.message}")
    }

    protected val safeScope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

}