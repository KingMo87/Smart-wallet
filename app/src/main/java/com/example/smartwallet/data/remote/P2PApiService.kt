package com.example.smartwallet.data.remote

import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.TransactionStatus
import com.example.smartwallet.domain.model.User

interface P2PApiService {
    suspend fun getContacts(): List<User>
    suspend fun sendMoney(
        from: User,
        to: User,
        amount: Double,
        currency: String,
        description: String?
    ): Transaction

    suspend fun requestMoney(
        from: User,
        to: User,
        amount: Double,
        currency: String,
        description: String?
    ): Transaction

    suspend fun getTransactions(userId: String): List<Transaction>
}

class FakeP2PApiService : P2PApiService {
    private val users = mutableListOf(
        User("user_1", "Avery Chen", "+1 555 100 1000", null, 1250.0),
        User("user_2", "Jordan Lee", "+1 555 200 1000", null, 800.0),
        User("user_3", "Priya Patel", "+1 555 300 1000", null, 450.0)
    )
    private val transactions = mutableListOf<Transaction>()

    override suspend fun getContacts(): List<User> = users

    override suspend fun sendMoney(
        from: User,
        to: User,
        amount: Double,
        currency: String,
        description: String?
    ): Transaction {
        val debit = from.copy(balance = from.balance - amount)
        val credit = to.copy(balance = to.balance + amount)
        updateUsers(debit, credit)
        val transaction = Transaction(
            id = (transactions.maxOfOrNull { it.id } ?: 0) + 1,
            fromUserId = from.id,
            toUserId = to.id,
            amount = amount,
            currency = currency,
            status = TransactionStatus.COMPLETED,
            createdAt = System.currentTimeMillis(),
            description = description
        )
        transactions += transaction
        return transaction
    }

    override suspend fun requestMoney(
        from: User,
        to: User,
        amount: Double,
        currency: String,
        description: String?
    ): Transaction {
        val transaction = Transaction(
            id = (transactions.maxOfOrNull { it.id } ?: 0) + 1,
            fromUserId = from.id,
            toUserId = to.id,
            amount = amount,
            currency = currency,
            status = TransactionStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            description = description
        )
        transactions += transaction
        return transaction
    }

    override suspend fun getTransactions(userId: String): List<Transaction> =
        transactions.filter { it.fromUserId == userId || it.toUserId == userId }

    private fun updateUsers(vararg updated: User) {
        updated.forEach { user ->
            val index = users.indexOfFirst { it.id == user.id }
            if (index != -1) {
                users[index] = user
            }
        }
    }
}
