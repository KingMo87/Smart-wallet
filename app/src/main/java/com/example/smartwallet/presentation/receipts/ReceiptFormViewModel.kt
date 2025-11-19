package com.example.smartwallet.presentation.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.usecase.GetReceiptUseCase
import com.example.smartwallet.domain.usecase.SaveReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptFormViewModel @Inject constructor(
    private val getReceiptUseCase: GetReceiptUseCase,
    private val saveReceiptUseCase: SaveReceiptUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReceiptFormUiState())
    val state: StateFlow<ReceiptFormUiState> = _state

    fun load(id: Long?) {
        if (id == null) return
        viewModelScope.launch {
            val receipt = getReceiptUseCase(id)
            receipt?.let {
                _state.value = ReceiptFormUiState.fromReceipt(it)
            }
        }
    }

    fun update(block: ReceiptFormUiState.() -> ReceiptFormUiState) {
        _state.value = _state.value.block()
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val current = _state.value
            val receipt = Receipt(
                id = current.id,
                merchantName = current.merchantName,
                date = current.date,
                amount = current.amount,
                currency = current.currency,
                category = current.category,
                paymentMethod = current.paymentMethod,
                imageUri = current.imageUri,
                notes = current.notes,
                createdAt = current.createdAt ?: now,
                updatedAt = now
            )
            saveReceiptUseCase(receipt)
            onDone()
        }
    }
}

data class ReceiptFormUiState(
    val id: Long = 0,
    val merchantName: String = "",
    val date: Long = System.currentTimeMillis(),
    val amount: Double = 0.0,
    val currency: String = "USD",
    val category: String = "General",
    val paymentMethod: String = "Card",
    val imageUri: String? = null,
    val notes: String? = null,
    val createdAt: Long? = null
) {
    companion object {
        fun fromReceipt(receipt: Receipt) = ReceiptFormUiState(
            id = receipt.id,
            merchantName = receipt.merchantName,
            date = receipt.date,
            amount = receipt.amount,
            currency = receipt.currency,
            category = receipt.category,
            paymentMethod = receipt.paymentMethod,
            imageUri = receipt.imageUri,
            notes = receipt.notes,
            createdAt = receipt.createdAt
        )
    }
}
