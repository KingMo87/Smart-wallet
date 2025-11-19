package com.example.smartwallet.domain.model

data class Transaction(
    val id: Long = 0,
    val fromUserId: String,
    val toUserId: String,
    val amount: Double,
    val currency: String,
    val status: TransactionStatus,
    val createdAt: Long,
    val description: String?
)
