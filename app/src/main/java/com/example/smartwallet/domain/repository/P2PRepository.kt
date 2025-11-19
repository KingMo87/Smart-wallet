package com.example.smartwallet.domain.repository

import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.User
import kotlinx.coroutines.flow.Flow

interface P2PRepository {
    val currentUser: Flow<User>
    fun observeTransactions(): Flow<List<Transaction>>
    suspend fun refreshTransactions()
    suspend fun sendMoney(toUserId: String, amount: Double, currency: String, note: String?)
    suspend fun requestMoney(fromUserId: String, amount: Double, currency: String, note: String?)
    suspend fun getContacts(): List<User>
}
