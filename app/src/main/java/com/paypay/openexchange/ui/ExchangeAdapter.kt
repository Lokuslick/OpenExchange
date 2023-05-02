package com.paypay.openexchange.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paypay.openexchange.model.BaseDTO
import com.paypay.openexchange.model.exchange.Rate

class ExchangeAdapter(private var exchangeList: List<Rate>?) :
    RecyclerView.Adapter<ExchangeViewHolder>() {

    var actionListener: ((type: BaseDTO, view: View) -> Unit)? = null

    companion object {
        const val EXCHANGE_RATE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        return when (viewType) {
            EXCHANGE_RATE -> ExchangeViewHolder.from(parent)
            else -> {
                throw IllegalStateException("UnnknownView")
            }
        }
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        exchangeList?.get(position)?.let {
            if (holder is ExchangeViewHolder && it is Rate) {
                holder.bind(it)
            }
        }
    }

    fun setValues(listRate: List<Rate>) {
        exchangeList = listRate
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return exchangeList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (exchangeList?.get(position)) {
            is Rate -> EXCHANGE_RATE
            else -> {
                -1
            }
        }
    }

}