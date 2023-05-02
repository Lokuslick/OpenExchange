package com.paypay.openexchange.database

import androidx.room.*
import com.paypay.openexchange.model.exchange.Rate

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM rate")
    fun getAll(): List<Rate>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rates: List<Rate>)

    @Delete
    fun delete(rates: Rate)

    @Delete
    fun deleteAll(rates: List<Rate>)

}