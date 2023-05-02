package com.paypay.openexchange.di

import android.content.Context
import androidx.room.Room
import com.paypay.openexchange.database.AppDatabase
import com.paypay.openexchange.database.ExchangeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    fun provideExchangeDao(appDatabase: AppDatabase): ExchangeDao {
        return appDatabase.exchangeDao()
    }
}