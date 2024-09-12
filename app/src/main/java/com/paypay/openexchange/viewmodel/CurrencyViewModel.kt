package com.paypay.openexchange.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.openexchange.model.network.ApiResult
import com.paypay.openexchange.model.exchange.ExchangeRate
import com.paypay.openexchange.model.exchange.Rate
import com.paypay.openexchange.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


class CurrencyViewModel
@ViewModelInject
constructor(private val repository: CurrencyRepository) : ViewModel() {

    private val _currencyMutableData = MutableLiveData<ApiResult<Map<String, String>?>>()
    val currencyLiveData = _currencyMutableData

    private val _exchangeMutableData = MutableLiveData<ApiResult<ExchangeRate?>?>()
    val exchangeLiveData = _exchangeMutableData

    fun getCurrencyList() {
        viewModelScope.launch {
            repository.getCurrencyList().collect {
                _currencyMutableData.value = it
            }
        }
    }

    fun getExchangeRate() {
        GlobalScope.launch {
            repository.getExchangeRate().collect {
                _exchangeMutableData.value = it
            }
        }
    }

    fun getConversionRate(
        rates: Map<String, Float>?,
        selectedRate: String,
        sourceAmount: Float
    ): List<Rate> {
        val rateList = arrayListOf<Rate>()
        val baseRate = rates?.get(selectedRate)
        rates?.keys?.forEach {
            val rate = Rate(
                it, convertCurrency(
                    baseRate ?: 1f,
                    rates[it] ?: 1f, sourceAmount
                )
            )
            rateList.add(rate)
        }
        return rateList
    }

    private fun convertCurrency(baseRate: Float, targetRate: Float, sourceAmount: Float): Float {
        return sourceAmount * (targetRate / baseRate)
    }
}