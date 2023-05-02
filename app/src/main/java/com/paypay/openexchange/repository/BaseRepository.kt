package com.paypay.openexchange.repository

import com.paypay.openexchange.model.network.ApiResult

import com.paypay.openexchange.model.network.ServerError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseRepository {

    suspend fun <T> apiCall(
        io: CoroutineDispatcher,
        network: suspend () -> Response<T>
    ): ApiResult<T?> {
        return withContext(io) {
            try {
                val response = network.invoke()
                if (response != null) {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200 -> ApiResult.Success(response.body())
                            else -> ApiResult.SuccessError(response.body())
                        }
                    } else {
                        ApiResult.Error(response)
                    }
                } else {
                    ApiResult.NullError("Something went wrong")
                }
            } catch (e: Exception) {
                ApiResult.Failure(ServerError(e.message, e))
            }
        }
    }
}
