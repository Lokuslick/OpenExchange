package com.paypay.openexchange.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.paypay.openexchange.R
import com.paypay.openexchange.databinding.ActivityMainBinding
import com.paypay.openexchange.util.gone
import com.paypay.openexchange.model.network.ApiResult
import com.paypay.openexchange.model.exchange.ExchangeRate
import com.paypay.openexchange.util.show
import com.paypay.openexchange.viewmodel.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CurrencyViewModel by viewModels()
    private lateinit var exchangeAdapter: ExchangeAdapter
    private var selectedRate = "AED"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.getCurrencyList()
        observeCurrencyLiveData()
        initListener()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        exchangeAdapter = ExchangeAdapter(ArrayList())
        binding.rvExchangeRates.adapter = exchangeAdapter
    }

    private fun initListener() {
        binding.valueEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.getExchangeRate()
            }
        })
    }

    private fun observeCurrencyLiveData() {
        viewModel.currencyLiveData.observe(this) { response ->
            when (response) {
                is ApiResult.Loading -> binding.progress.show()
                is ApiResult.Success -> setCurrencySuccessData(response.data)
                is ApiResult.Error -> setFailure(response.error.message())
                else -> {
                    //no-ops
                }
            }
        }
        viewModel.exchangeLiveData.observe(this) { response ->
            when (response) {
                is ApiResult.Loading -> binding.progress.show()
                is ApiResult.Success -> setExchangeSuccessData(response.data)
                is ApiResult.Error -> setFailure(response.error.message())
                else -> {
                    //no-ops
                }
            }
        }
    }

    private fun setExchangeSuccessData(data: ExchangeRate?) {
        binding.progress.gone()
        if (binding.valueEt.text?.isNotEmpty() == true) {
            val conversionList = viewModel.getConversionRate(
                data?.rates,
                selectedRate,
                binding.valueEt.text?.toString()?.toFloat() ?: 0f
            )
            exchangeAdapter.setValues(conversionList)
        } else {
            setZeroValues(data)
        }
    }

    private fun setZeroValues(data: ExchangeRate?) {
        val conversionList = viewModel.getConversionRate(
            data?.rates,
            selectedRate,
            0f
        )
        exchangeAdapter.setValues(conversionList)
    }

    private fun setFailure(message: String) {
        binding.progress.gone()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    private fun setCurrencySuccessData(currencyMap: Map<String, String>?) {
        binding.progress.gone()
        val listOfCurrencies = currencyMap?.keys?.toList()
        val currencyAppended = currencyMap?.map { "${it.key} - ${it.value}" }
        val currencyAdapter =
            ArrayAdapter(this, R.layout.exchange_rate_spinner, currencyAppended ?: emptyList())
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.currencySpinner.adapter = currencyAdapter
        binding.currencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                ) {
                    selectedRate = listOfCurrencies?.get(pos).toString()
                    if (binding.valueEt.text?.isNotEmpty() == true) {
                        viewModel.getExchangeRate()
                    } else {
                        Toast.makeText(view?.context, "Please enter the value", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //no-op
                }
            }

    }
}