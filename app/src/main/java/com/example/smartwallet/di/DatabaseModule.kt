package com.example.smartwallet.di

import android.content.Context
import androidx.room.Room
import com.example.smartwallet.data.local.SmartWalletDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartWalletDatabase =
        Room.databaseBuilder(
            context,
            SmartWalletDatabase::class.java,
            "smart_wallet.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideReceiptDao(database: SmartWalletDatabase) = database.receiptDao()

    @Provides
    fun provideTransactionDao(database: SmartWalletDatabase) = database.transactionDao()
}
