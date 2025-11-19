package com.example.smartwallet

import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.User
import com.example.smartwallet.domain.repository.P2PRepository
import com.example.smartwallet.domain.usecase.GetContactsUseCase
import com.example.smartwallet.domain.usecase.ObserveCurrentUserUseCase
import com.example.smartwallet.domain.usecase.ObserveTransactionsUseCase
import com.example.smartwallet.domain.usecase.RequestMoneyUseCase
import com.example.smartwallet.domain.usecase.SendMoneyUseCase
import com.example.smartwallet.presentation.p2p.P2PHomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class P2PHomeViewModelTest {
    private val userFlow = MutableStateFlow(User("me", "Me", "", null, 100.0))
    private val transactionFlow = MutableStateFlow(emptyList<Transaction>())
    private val contacts = listOf(User("friend", "Friend", "", null, 50.0))

    private val repository = object : P2PRepository {
        override val currentUser: Flow<User> = userFlow
        override fun observeTransactions(): Flow<List<Transaction>> = transactionFlow
        override suspend fun refreshTransactions() {}
        override suspend fun sendMoney(toUserId: String, amount: Double, currency: String, note: String?) {}
        override suspend fun requestMoney(fromUserId: String, amount: Double, currency: String, note: String?) {}
        override suspend fun getContacts(): List<User> = contacts
    }

    @Test
    fun contactsAreLoaded() = runTest {
        val viewModel = P2PHomeViewModel(
            ObserveCurrentUserUseCase(repository),
            ObserveTransactionsUseCase(repository),
            GetContactsUseCase(repository),
            SendMoneyUseCase(repository),
            RequestMoneyUseCase(repository)
        )
        val state = viewModel.state.value
        assertThat(state.contacts).isNotEmpty()
    }
}
