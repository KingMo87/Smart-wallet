package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.repository.P2PRepository
import javax.inject.Inject

class RequestMoneyUseCase @Inject constructor(
    private val repository: P2PRepository
) {
    suspend operator fun invoke(fromUserId: String, amount: Double, currency: String, note: String?) =
        repository.requestMoney(fromUserId, amount, currency, note)
}
