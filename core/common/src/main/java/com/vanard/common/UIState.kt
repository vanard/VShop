package com.vanard.core.common

sealed class UIState<out T : Any?> {

    data object Loading : UIState<Nothing>()

//    data class Success<out T : Any>(val data: T) : UIState<T>()
    data class Success<out T>(val data: T) : UIState<T>()

    data class Error(val errorMessage: String) : UIState<Nothing>()
}