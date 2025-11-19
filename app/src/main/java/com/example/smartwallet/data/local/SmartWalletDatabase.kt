package com.example.smartwallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartwallet.data.local.dao.ReceiptDao
import com.example.smartwallet.data.local.dao.TransactionDao
import com.example.smartwallet.data.local.entity.ReceiptEntity
import com.example.smartwallet.data.local.entity.TransactionEntity
import com.example.smartwallet.data.local.util.TransactionStatusConverter

@Database(
    entities = [ReceiptEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransactionStatusConverter::class)
abstract class SmartWalletDatabase : RoomDatabase() {
    abstract fun receiptDao(): ReceiptDao
    abstract fun transactionDao(): TransactionDao
}
