package com.paypay.openexchange.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paypay.openexchange.databinding.ItemExchangeBinding
import com.paypay.openexchange.model.BaseDTO
import com.paypay.openexchange.model.exchange.Rate

class ExchangeViewHolder(val binding: ItemExchangeBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(rate: Rate) {
        binding.rate = rate
        binding.executePendingBindings()
    }

    companion object {
        fun from(viewGroup: ViewGroup): ExchangeViewHolder {
            val layoutInflater = LayoutInflater.from(viewGroup.context)
            val binding = ItemExchangeBinding.inflate(layoutInflater)
            return ExchangeViewHolder(binding)
        }
    }
}