package com.paypay.openexchange.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paypay.openexchange.model.exchange.Rate

@Database(entities = [Rate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeDao(): ExchangeDao
}