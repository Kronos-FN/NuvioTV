package com.nuvio.tv.core.network

import com.nuvio.tv.util.Logger

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: Exception) {
        Logger.e("SafeApiCall", "API call failed", e)
        NetworkResult.Error(e.message ?: "Unknown error occurred")
    }
}
