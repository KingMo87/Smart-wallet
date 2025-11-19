package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.repository.ReceiptRepository
import javax.inject.Inject

class GetReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(id: Long): Receipt? = repository.getReceipt(id)
}
