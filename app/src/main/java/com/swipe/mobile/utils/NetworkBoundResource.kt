package com.swipe.mobile.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            val response = fetch()
            saveFetchResult(response)
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable.localizedMessage ?: "unknown error", it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}

@ExperimentalCoroutinesApi
inline fun <ResultType> fetchOnly(
    crossinline fetch: suspend () -> ResultType
): Flow<Resource<ResultType>> = channelFlow {
    send(Resource.Loading<ResultType>())
    try {
        val response = fetch()
        send(Resource.Success(response))
    } catch (throwable: Throwable) {
        send(Resource.Error<ResultType>(throwable.localizedMessage ?: "unknown error"))
    }
}