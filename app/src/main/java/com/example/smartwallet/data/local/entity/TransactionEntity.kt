package com.example.smartwallet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartwallet.domain.model.TransactionStatus

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromUserId: String,
    val toUserId: String,
    val amount: Double,
    val currency: String,
    val status: TransactionStatus,
    val createdAt: Long,
    val description: String?
)
