package com.example.smartwallet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val merchantName: String,
    val date: Long,
    val amount: Double,
    val currency: String,
    val category: String,
    val paymentMethod: String,
    val imageUri: String?,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long
)
