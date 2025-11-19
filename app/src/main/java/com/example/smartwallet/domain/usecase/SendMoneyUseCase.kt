package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.repository.P2PRepository
import javax.inject.Inject

class SendMoneyUseCase @Inject constructor(
    private val repository: P2PRepository
) {
    suspend operator fun invoke(toUserId: String, amount: Double, currency: String, note: String?) =
        repository.sendMoney(toUserId, amount, currency, note)
}
