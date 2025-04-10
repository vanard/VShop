package com.vanard.core.common

import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

const val UNSUCCESSFUL_MSG = "Something went wrong"
const val SERVER_ERROR = "Server error?"
const val INVALID_JSON_FORMAT =
    "The data couldn't be read because it isn't in the correct format"

suspend fun <T : Any> fetchState(call: suspend () -> Response<T>): UIState<T> {
    val response: Response<T>
    return try {
        response = call.invoke()
        when (response.code()) {
            in 200..201 -> {
                if (response.body() != null)
                    UIState.Success(response.body()!!)
                else
                    UIState.Error(UNSUCCESSFUL_MSG)
            }

            else -> UIState.Error(UNSUCCESSFUL_MSG)
        }
    } catch (throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SSLHandshakeException, is SSLException, is IOException ->
                UIState.Error(SERVER_ERROR)

            is JsonSyntaxException -> UIState.Error(INVALID_JSON_FORMAT)
            else -> {
                UIState.Error(UNSUCCESSFUL_MSG)
            }
        }
    }
}

inline fun <T, R> UIState<T>.map(transform: (T) -> R): UIState<R> = when (this) {
    is UIState.Success -> UIState.Success(transform(data))
    is UIState.Error -> this
    is UIState.Loading -> this
}