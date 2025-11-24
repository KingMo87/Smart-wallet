package com.example.smartwallet.data.repository

import com.example.smartwallet.data.local.dao.TransactionDao
import com.example.smartwallet.data.local.entity.TransactionEntity
import com.example.smartwallet.data.mapper.toDomain
import com.example.smartwallet.data.mapper.toEntity
import com.example.smartwallet.data.remote.P2PApiService
import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.TransactionStatus
import com.example.smartwallet.domain.model.User
import com.example.smartwallet.domain.repository.P2PRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class P2PRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val apiService: P2PApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : P2PRepository {

    private val seedScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentUser = MutableStateFlow(
        User(
            id = "user_0",
            displayName = "You",
            phoneNumber = "+1 555 000 0000",
            avatarUrl = null,
            balance = 1650.0
        )
    )
    override val currentUser: Flow<User> = _currentUser.asStateFlow()

    init {
        seedScope.launch {
            if (transactionDao.observeTransactions().first().isEmpty()) {
                val now = System.currentTimeMillis()
                listOf(
                    TransactionEntity(
                        fromUserId = "user_0",
                        toUserId = "user_2",
                        amount = 35.0,
                        currency = "USD",
                        status = TransactionStatus.COMPLETED,
                        createdAt = now - 3_600_000,
                        description = "Lunch split"
                    ),
                    TransactionEntity(
                        fromUserId = "user_3",
                        toUserId = "user_0",
                        amount = 58.0,
                        currency = "USD",
                        status = TransactionStatus.PENDING,
                        createdAt = now - 7_200_000,
                        description = "Utilities"
                    )
                ).forEach { transactionDao.upsert(it) }
            }
        }
    }

    override fun observeTransactions(): Flow<List<Transaction>> =
        transactionDao.observeTransactions()
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshTransactions(): Unit = withContext(dispatcher) {
        val remote = apiService.getTransactions(_currentUser.value.id)
        remote.forEach { transactionDao.upsert(it.toEntity()) }
    }

    override suspend fun sendMoney(
        toUserId: String,
        amount: Double,
        currency: String,
        note: String?
    ): Unit = withContext(dispatcher) {

        val contacts = apiService.getContacts()

        val recipient = contacts.firstOrNull { it.id == toUserId }
            ?: throw IllegalArgumentException("Recipient with id=$toUserId not found")

        val sender = _currentUser.value

        val transaction = apiService.sendMoney(
            sender = sender,
            receiver = recipient,
            amount = amount,
            currency = currency,
            note = note
        )

        transactionDao.upsert(transaction.toEntity())
        _currentUser.value = sender.copy(balance = sender.balance - amount)
    }

    override suspend fun requestMoney(
        fromUserId: String,
        amount: Double,
        currency: String,
        note: String?
    ): Unit = withContext(dispatcher) {

        val contacts = apiService.getContacts()

        val requester = contacts.firstOrNull { it.id == fromUserId }
            ?: throw IllegalArgumentException("User with id=$fromUserId not found")

        val currentUser = _currentUser.value

        val transaction = apiService.requestMoney(
            requester = requester,
            receiver = currentUser,
            amount = amount,
            currency = currency,
            note = note
        )

        transactionDao.upsert(transaction.toEntity())
    }

    override suspend fun getContacts(): List<User> = withContext(dispatcher) {
        apiService.getContacts()
    }
}
