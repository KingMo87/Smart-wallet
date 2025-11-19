package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.repository.ReceiptRepository
import javax.inject.Inject

class SaveReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: Receipt): Receipt = repository.upsert(receipt)
}
