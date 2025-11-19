package com.example.smartwallet.domain.repository

import com.example.smartwallet.domain.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {
    fun observeReceipts(): Flow<List<Receipt>>
    suspend fun getReceipt(id: Long): Receipt?
    suspend fun upsert(receipt: Receipt): Receipt
    suspend fun delete(receipt: Receipt)
    suspend fun search(query: String): List<Receipt>
}
