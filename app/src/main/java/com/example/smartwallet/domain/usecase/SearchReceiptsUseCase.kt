package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.repository.ReceiptRepository
import javax.inject.Inject

class SearchReceiptsUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(query: String): List<Receipt> = repository.search(query)
}
