package com.paypay.openexchange.model.exchange

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeRate(
    val base: String?,
    val rates: Map<String, Float>
) : Parcelable


