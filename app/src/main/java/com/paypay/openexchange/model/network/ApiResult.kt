package com.paypay.openexchange.model.network

import retrofit2.Response

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class SuccessError<out T>(val successError: T) : ApiResult<T>()
    data class Error(val error: Response<*>) : ApiResult<Nothing>()
    data class Failure(val failure: ServerError) : ApiResult<Nothing>()
    data class NullError(val successError: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()

}
