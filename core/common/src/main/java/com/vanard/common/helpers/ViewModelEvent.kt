package com.vanard.common.helpers

/**
 * Base class for one-time events in ViewModels
 * Used for navigation, showing toasts, etc.
 */
sealed class ViewModelEvent {
    /**
     * Session-related events
     */
    sealed class SessionEvent : ViewModelEvent() {
        object SessionExpired : SessionEvent()
        object Unauthorized : SessionEvent()
        object LoggedOut : SessionEvent()
    }

    /**
     * Navigation events
     */
    sealed class NavigationEvent : ViewModelEvent() {
        data class NavigateTo(val route: String) : NavigationEvent()
        object NavigateBack : NavigationEvent()
        data class NavigateToWithClear(val route: String) : NavigationEvent()
    }

    /**
     * Message events
     */
    sealed class MessageEvent : ViewModelEvent() {
        data class ShowToast(val message: String) : MessageEvent()
        data class ShowSnackbar(val message: String, val action: String? = null) : MessageEvent()
        data class ShowError(val message: String) : MessageEvent()
    }
}

/**
 * Wrapper for one-time event consumption
 */
data class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
