package com.paypay.openexchange.model.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rate(@PrimaryKey val key: String, val value: Float)