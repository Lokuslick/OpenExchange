package com.paypay.openexchange.network

import com.paypay.openexchange.model.exchange.ExchangeRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("api/currencies.json")
    suspend fun getCurrencies(
        @Query("prettyprint") prettyprint: Boolean? = false,
        @Query("show_alternative") show_alternative: Boolean? = false,
        @Query("show_inactive") show_inactive: Boolean? = false
    ): Response<Map<String, String>>

    @GET("api/latest.json")
    suspend fun getExchangeRate(@Query("base") process: String? = "USD"): Response<ExchangeRate>

}