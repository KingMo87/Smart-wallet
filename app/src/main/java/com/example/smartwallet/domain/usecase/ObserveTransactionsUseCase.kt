package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.repository.P2PRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTransactionsUseCase @Inject constructor(
    private val repository: P2PRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.observeTransactions()
}
