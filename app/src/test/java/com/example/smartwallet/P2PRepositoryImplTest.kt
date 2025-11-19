package com.example.smartwallet

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.smartwallet.data.local.SmartWalletDatabase
import com.example.smartwallet.data.remote.FakeP2PApiService
import com.example.smartwallet.data.repository.P2PRepositoryImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class P2PRepositoryImplTest {
    private lateinit var database: SmartWalletDatabase
    private lateinit var repository: P2PRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, SmartWalletDatabase::class.java).allowMainThreadQueries().build()
        repository = P2PRepositoryImpl(database.transactionDao(), FakeP2PApiService())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun sendMoneyCreatesTransaction() = runTest {
        repository.sendMoney("user_2", 12.0, "USD", "Coffee")
        val transactions = repository.observeTransactions().first()
        assertThat(transactions).isNotEmpty()
    }
}
