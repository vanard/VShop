package com.vanard.common.helpers

import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Base exception class for application-specific errors
 */
sealed class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Authentication-related errors (401, 403)
     */
    sealed class AuthException(message: String, cause: Throwable? = null) :
        AppException(message, cause) {
        class Unauthorized(message: String = "Unauthorized access", cause: Throwable? = null) :
            AuthException(message, cause)

        class SessionExpired(message: String = "Session has expired", cause: Throwable? = null) :
            AuthException(message, cause)

        class InvalidToken(
            message: String = "Invalid or malformed token",
            cause: Throwable? = null
        ) :
            AuthException(message, cause)

        class Forbidden(message: String = "Access forbidden", cause: Throwable? = null) :
            AuthException(message, cause)
    }

    /**
     * Network-related errors
     */
    sealed class NetworkException(message: String, cause: Throwable? = null) :
        AppException(message, cause) {
        class NoConnection(message: String = "No internet connection", cause: Throwable? = null) :
            NetworkException(message, cause)

        class Timeout(message: String = "Request timeout", cause: Throwable? = null) :
            NetworkException(message, cause)

        class ServerError(message: String = "Server error", cause: Throwable? = null) :
            NetworkException(message, cause)
    }

    /**
     * Data/validation errors
     */
    sealed class DataException(message: String, cause: Throwable? = null) :
        AppException(message, cause) {
        class NotFound(message: String = "Resource not found", cause: Throwable? = null) :
            DataException(message, cause)

        class InvalidData(message: String = "Invalid data format", cause: Throwable? = null) :
            DataException(message, cause)

        class ParseError(message: String = "Failed to parse data", cause: Throwable? = null) :
            DataException(message, cause)
    }

    /**
     * Generic unknown error
     */
    class Unknown(message: String = "An unknown error occurred", cause: Throwable? = null) :
        AppException(message, cause)
}

/**
 * Extension to check if exception is auth-related
 */
fun Throwable.isAuthException(): Boolean = this is AppException.AuthException

/**
 * Extension to check if exception requires re-authentication
 */
fun Throwable.requiresReauth(): Boolean = when (this) {
    is AppException.AuthException.Unauthorized,
    is AppException.AuthException.SessionExpired,
    is AppException.AuthException.InvalidToken -> true

    else -> false
}

/**
 * Convert generic exceptions to AppException
 */
fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is HttpException -> {
            when (code()) {
                401 -> AppException.AuthException.Unauthorized(
                    message = message() ?: "Unauthorized",
                    cause = this
                )

                403 -> AppException.AuthException.Forbidden(
                    message = message() ?: "Forbidden",
                    cause = this
                )

                404 -> AppException.DataException.NotFound(
                    message = message() ?: "Not found",
                    cause = this
                )

                in 500..599 -> AppException.NetworkException.ServerError(
                    message = message() ?: "Server error",
                    cause = this
                )

                else -> AppException.Unknown(
                    message = message() ?: "HTTP error ${code()}",
                    cause = this
                )
            }
        }

        is UnknownHostException -> AppException.NetworkException.NoConnection(cause = this)
        is SocketTimeoutException -> AppException.NetworkException.Timeout(cause = this)
        is IOException -> AppException.NetworkException.NoConnection(
            message = "Connection error",
            cause = this
        )

        is JsonSyntaxException -> AppException.DataException.ParseError(
            message = "Failed to parse JSON",
            cause = this
        )

        else -> AppException.Unknown(
            message = this.message ?: "Unknown error",
            cause = this
        )
    }
}
