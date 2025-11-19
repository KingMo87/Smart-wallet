package com.example.smartwallet.presentation.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.usecase.DeleteReceiptUseCase
import com.example.smartwallet.domain.usecase.GetReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptDetailViewModel @Inject constructor(
    private val getReceiptUseCase: GetReceiptUseCase,
    private val deleteReceiptUseCase: DeleteReceiptUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ReceiptDetailUiState>(ReceiptDetailUiState.Loading)
    val state: StateFlow<ReceiptDetailUiState> = _state

    fun loadReceipt(id: Long) {
        viewModelScope.launch {
            _state.value = ReceiptDetailUiState.Loading
            val receipt = getReceiptUseCase(id)
            _state.value = receipt?.let { ReceiptDetailUiState.Success(it) }
                ?: ReceiptDetailUiState.Error("Receipt not found")
        }
    }

    fun delete(receipt: Receipt, onDeleted: () -> Unit) {
        viewModelScope.launch {
            deleteReceiptUseCase(receipt)
            onDeleted()
        }
    }
}

sealed interface ReceiptDetailUiState {
    data object Loading : ReceiptDetailUiState
    data class Success(val receipt: Receipt) : ReceiptDetailUiState
    data class Error(val message: String) : ReceiptDetailUiState
}
