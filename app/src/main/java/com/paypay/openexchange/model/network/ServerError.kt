package com.paypay.openexchange.model.network

data class ServerError(
    val message: String?,
    val throwable: Throwable?
)
