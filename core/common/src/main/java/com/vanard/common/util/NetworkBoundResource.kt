package com.vanard.common.util

import com.vanard.common.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T> networkBoundResource(
    query: () -> Flow<T>,
    fetch: suspend () -> T,
    saveFetchResult: suspend (T) -> Unit,
    shouldFetch: (T) -> Boolean = { true }
): Flow<T> = flow {
    val data = query().first() // get initial cache
    emit(data)

    if (shouldFetch(data)) {
        try {
            val fetched = fetch()
            saveFetchResult(fetched)
        } catch (e: Exception) {
            // Optionally handle fetch error
        }
    }

    emitAll(query()) // re-emit updated cache
}

fun <ResultType, RequestType> networkBoundResourceUiState(
    query: () -> Flow<ResultType>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
): Flow<UIState<ResultType>> = flow {
    emit(UIState.Loading)

    val data = query().firstOrNull()
    if (data != null) emit(UIState.Success(data))

    if (data == null || shouldFetch(data)) {
        try {
            val remoteData = fetch()
            saveFetchResult(remoteData)
            emitAll(query().map { UIState.Success(it) })
        } catch (e: Exception) {
            emit(UIState.Error(e.message ?: "Unknown error"))
        }
    } else {
        emitAll(query().map { UIState.Success(it) })
    }
}