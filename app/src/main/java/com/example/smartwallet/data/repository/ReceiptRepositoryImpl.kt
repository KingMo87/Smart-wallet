package com.example.smartwallet.data.repository

import com.example.smartwallet.data.local.dao.ReceiptDao
import com.example.smartwallet.data.local.entity.ReceiptEntity
import com.example.smartwallet.data.mapper.toDomain
import com.example.smartwallet.data.mapper.toEntity
import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.repository.ReceiptRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReceiptRepositoryImpl @Inject constructor(
    private val receiptDao: ReceiptDao
) : ReceiptRepository {

    private val seedScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        seedScope.launch {
            if (receiptDao.observeReceipts().first().isEmpty()) {
                val now = System.currentTimeMillis()
                listOf(
                    ReceiptEntity(
                        merchantName = "Whole Foods",
                        date = now,
                        amount = 82.45,
                        currency = "USD",
                        category = "Groceries",
                        paymentMethod = "Card",
                        imageUri = null,
                        notes = "Weekly groceries",
                        createdAt = now,
                        updatedAt = now
                    ),
                    ReceiptEntity(
                        merchantName = "Uber",
                        date = now - 86_400_000,
                        amount = 23.10,
                        currency = "USD",
                        category = "Transport",
                        paymentMethod = "Google Pay",
                        imageUri = null,
                        notes = "Airport ride",
                        createdAt = now - 86_400_000,
                        updatedAt = now - 86_400_000
                    )
                ).forEach { receiptDao.upsert(it) }
            }
        }
    }
    override fun observeReceipts(): Flow<List<Receipt>> =
        receiptDao.observeReceipts().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getReceipt(id: Long): Receipt? = receiptDao.getReceipt(id)?.toDomain()

    override suspend fun upsert(receipt: Receipt): Receipt {
        val entity = receipt.toEntity()
        val assignedId = if (entity.id == 0L) receiptDao.upsert(entity) else {
            receiptDao.upsert(entity)
        }
        return receiptDao.getReceipt(if (entity.id == 0L) assignedId else entity.id)!!.toDomain()
    }

    override suspend fun delete(receipt: Receipt) {
        receiptDao.delete(receipt.toEntity())
    }

    override suspend fun search(query: String): List<Receipt> =
        receiptDao.searchReceipts(query).map { it.toDomain() }
}
