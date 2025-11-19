package com.example.smartwallet.domain.model

data class Receipt(
    val id: Long = 0,
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
