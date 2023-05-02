package com.paypay.openexchange.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.paypay.openexchange.model.network.ApiResult
import com.paypay.openexchange.model.network.ServerError
import com.paypay.openexchange.repository.CurrencyRepository
import com.paypay.openexchange.util.InstantExecutorExtension
import com.paypay.openexchange.util.MainCoroutineRule
import com.paypay.openexchange.util.TestRatesGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CurrencyViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var repository: CurrencyRepository = mockk()

    private val testRatesGenerator: TestRatesGenerator = TestRatesGenerator()

    private lateinit var viewModel: CurrencyViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Test
    fun `currency should set live data`() {
        // Given
        val currencyMap = mapOf("USD" to "US Dollar", "EUR" to "Euro")
        //1- Mock calls
        coEvery { repository.getCurrencyList() } returns flow {
            emit(ApiResult.Success(currencyMap))
        }
        //2-Call
        viewModel = CurrencyViewModel(repository)
        viewModel.getCurrencyList()
        //active observer for livedata
        viewModel.currencyLiveData.observeForever { }

        //3-verify
        var mapOfCurrency = mapOf<String, String>()
        val apiResult = viewModel.currencyLiveData.value
        if (apiResult is ApiResult.Success) {
            mapOfCurrency = apiResult.data!!
        }
        assertEquals(currencyMap, mapOfCurrency)
    }

    @Test
    fun `get currencyMap error`() {

        val error: ApiResult<Map<String, String>> =
            ApiResult.Failure(ServerError("Something went wrong", Exception("Test exception")))

        //1- Mock calls
        coEvery { repository.getCurrencyList() } returns flow {
            emit(error)
        }

        //2-Call
        viewModel = CurrencyViewModel(repository)
        viewModel.getCurrencyList()

        //active observer for livedata
        viewModel.currencyLiveData.observeForever { }

        assertEquals(
            error,
            viewModel.currencyLiveData.value
        )
    }

    @Test
    fun `get currency conversion rate`(){
        val rates = mapOf("USD" to 1.0f, "EUR" to 3.4f,"INR" to 82.2f)
        viewModel = CurrencyViewModel(repository)
        val currentRate = viewModel.getConversionRate(rates,"INR",100f)
        assertEquals(1.2165451f, currentRate.first().value)
        assertEquals(100.0f, currentRate.last().value)
    }

    @Test
    fun `given exchange rate api call it should get different rates live data`() {
        // Given
        val rateMock = testRatesGenerator.getExchangeRateMock()
        //1- Mock calls
        coEvery { repository.getExchangeRate() } returns flow {
            emit(ApiResult.Success(rateMock))
        }
        //2-Call
        viewModel = CurrencyViewModel(repository)
        viewModel.getExchangeRate()

        //active observer for livedata
        viewModel.exchangeLiveData.observeForever { }

        //3-verify
        val apiResult = viewModel.exchangeLiveData.value
        if (apiResult is ApiResult.Success) {
            assertEquals(rateMock.rates, apiResult.data?.rates)
        }
    }
}