package com.example.smartwallet.data.local.util

import androidx.room.TypeConverter
import com.example.smartwallet.domain.model.TransactionStatus

class TransactionStatusConverter {
    @TypeConverter
    fun fromStatus(status: TransactionStatus?): String? = status?.name

    @TypeConverter
    fun toStatus(name: String?): TransactionStatus? = name?.let { TransactionStatus.valueOf(it) }
}
