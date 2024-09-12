package com.paypay.openexchange.repository

import com.paypay.openexchange.util.Constants
import com.paypay.openexchange.database.ExchangeDao
import com.paypay.openexchange.database.SharedPref
import com.paypay.openexchange.database.SharedPrefKeys
import com.paypay.openexchange.model.network.ApiResult
import com.paypay.openexchange.model.exchange.ExchangeRate
import com.paypay.openexchange.model.exchange.Rate
import com.paypay.openexchange.network.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository
@Inject
constructor(private val apiInterface: ApiInterface, private val exchangeDao: ExchangeDao) :
    BaseRepository() {

    suspend fun getCurrencyList(): Flow<ApiResult<Map<String, String>?>> {
        return flow {
            emit(ApiResult.Loading)
            val result = apiCall(Dispatchers.IO) {
                apiInterface.getCurrencies()
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getExchangeRate(): Flow<ApiResult<ExchangeRate?>?> {
        return flow {
            emit(ApiResult.Loading)
            val cachedResult = fetchCachedExchangeRate()
            if (cachedResult is ApiResult.Success && !isLastTimeFetched30MinsBack()) {
                // If cached data is available, emit it and return from the function.
                emit(cachedResult)
                return@flow
            }

            // Fetch fresh data from the API if cached data is not available.
            val apiResult = apiCall(Dispatchers.Default) {
                SharedPref.save(SharedPrefKeys.LAST_TIME_FETCH, System.currentTimeMillis())
                apiInterface.getExchangeRate(Constants.BASE_CURENCY)
            }

            if (apiResult is ApiResult.Success) {
                getExchangeListFromMap(apiResult.data?.rates)?.let { exchangeDao.insertAll(it) }
            }
            emit(apiResult)
        }.flowOn(Dispatchers.IO)
    }

    private fun isLastTimeFetched30MinsBack(): Boolean {
        val difference = System.currentTimeMillis()
            .minus(SharedPref.getLongValue(SharedPrefKeys.LAST_TIME_FETCH))
        return difference > Constants.THIRTY_MINS
    }

    private fun getExchangeListFromMap(rates: Map<String, Float>?): List<Rate>? {
        return rates?.map { Rate(it.key, it.value) }
    }

    private fun fetchCachedExchangeRate(): ApiResult<ExchangeRate?>? =
        exchangeDao.getAll()?.let {
            ApiResult.Success(ExchangeRate(Constants.BASE_CURENCY, getListToMap(it)))
        }

    private fun getListToMap(list: List<Rate>): Map<String, Float> {
        return list.associateBy({ it.key }, { it.value })
    }

}