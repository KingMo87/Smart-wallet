package com.example.smartwallet

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.smartwallet.data.local.SmartWalletDatabase
import com.example.smartwallet.data.repository.ReceiptRepositoryImpl
import com.example.smartwallet.domain.model.Receipt
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReceiptRepositoryImplTest {
    private lateinit var database: SmartWalletDatabase
    private lateinit var repository: ReceiptRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, SmartWalletDatabase::class.java).allowMainThreadQueries().build()
        repository = ReceiptRepositoryImpl(database.receiptDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveReceipt() = runTest {
        val receipt = Receipt(
            merchantName = "Test Store",
            date = System.currentTimeMillis(),
            amount = 10.0,
            currency = "USD",
            category = "General",
            paymentMethod = "Card",
            imageUri = null,
            notes = null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val saved = repository.upsert(receipt)
        val list = repository.observeReceipts().first()
        assertThat(list).isNotEmpty()
        assertThat(list.first().merchantName).isEqualTo("Test Store")
        assertThat(saved.id).isGreaterThan(0)
    }
}
