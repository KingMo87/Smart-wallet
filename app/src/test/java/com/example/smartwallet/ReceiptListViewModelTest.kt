package com.example.smartwallet

import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.repository.ReceiptRepository
import com.example.smartwallet.domain.usecase.GetReceiptsUseCase
import com.example.smartwallet.domain.usecase.SearchReceiptsUseCase
import com.example.smartwallet.presentation.receipts.ReceiptListViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReceiptListViewModelTest {
    private val receiptsFlow = MutableStateFlow(listOf(sampleReceipt()))
    private val repository = object : ReceiptRepository {
        override fun observeReceipts(): Flow<List<Receipt>> = receiptsFlow
        override suspend fun getReceipt(id: Long): Receipt? = receiptsFlow.value.firstOrNull { it.id == id }
        override suspend fun upsert(receipt: Receipt): Receipt = receipt
        override suspend fun delete(receipt: Receipt) {}
        override suspend fun search(query: String): List<Receipt> = receiptsFlow.value.filter { it.merchantName.contains(query) }
    }

    private fun sampleReceipt() = Receipt(
        id = 1,
        merchantName = "Cafe",
        date = System.currentTimeMillis(),
        amount = 5.0,
        currency = "USD",
        category = "Dining",
        paymentMethod = "Card",
        imageUri = null,
        notes = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    @Test
    fun searchUpdatesFilteredList() = runTest {
        val viewModel = ReceiptListViewModel(GetReceiptsUseCase(repository), SearchReceiptsUseCase(repository))
        viewModel.onSearch("Cafe")
        val state = viewModel.uiState.value
        assertThat(state.filteredReceipts).hasSize(1)
    }
}
