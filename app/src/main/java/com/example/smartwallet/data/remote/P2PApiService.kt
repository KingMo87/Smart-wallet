package com.example.smartwallet.data.remote

import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.User

interface P2PApiService {

    suspend fun getContacts(): List<User>

    suspend fun sendMoney(
        sender: User,
        receiver: User,
        amount: Double,
        currency: String,
        note: String?
    ): Transaction

    suspend fun requestMoney(
        requester: User,
        receiver: User,
        amount: Double,
        currency: String,
        note: String?
    ): Transaction

    suspend fun getTransactions(userId: String): List<Transaction>
}
