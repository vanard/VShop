package com.vanard.common

sealed class UIState<out T> {

    object Idle : UIState<Nothing>()

    object Loading : UIState<Nothing>()

//    data class Success<out T : Any>(val data: T) : UIState<T>()
    data class Success<out T>(val data: T) : UIState<T>()

    data class Error(val errorMessage: String) : UIState<Nothing>()
}