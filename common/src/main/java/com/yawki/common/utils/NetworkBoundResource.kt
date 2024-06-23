package com.yawki.common.utils

import com.yawki.common.domain.SafeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <RequestType, ResponseType> networkBoundResource(
    crossinline query: () -> Flow<ResponseType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResponseType) -> Boolean = { true },
    crossinline onFetchSuccess: () -> Unit = { },
    crossinline onFetchFailed: (Exception) -> Unit = { }
) = flow {
    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(SafeResult.Loading)
        try {
            saveFetchResult(fetch())
            query().map { SafeResult.Success(it) }
        } catch (exception: Exception) {
            query().map { SafeResult.Error(exception, "Error at api request.") }
        }
    } else {
        query().map { SafeResult.Success(it) }
    }
    emitAll(flow)
}