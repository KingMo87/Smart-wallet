package com.example.smartwallet.presentation.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartwallet.domain.model.Receipt
import com.example.smartwallet.domain.usecase.GetReceiptsUseCase
import com.example.smartwallet.domain.usecase.SearchReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptListViewModel @Inject constructor(
    private val getReceiptsUseCase: GetReceiptsUseCase,
    private val searchReceiptsUseCase: SearchReceiptsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptListUiState())
    val uiState: StateFlow<ReceiptListUiState> = _uiState

    init {
        viewModelScope.launch {
            getReceiptsUseCase().collectLatest { receipts ->
                _uiState.update { it.copy(isLoading = false, receipts = receipts, filteredReceipts = receipts) }
            }
        }
    }

    fun onSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.update { it.copy(filteredReceipts = it.receipts) }
            } else {
                val result = searchReceiptsUseCase(query)
                _uiState.update { it.copy(filteredReceipts = result) }
            }
        }
    }
}

data class ReceiptListUiState(
    val isLoading: Boolean = true,
    val receipts: List<Receipt> = emptyList(),
    val filteredReceipts: List<Receipt> = emptyList(),
    val searchQuery: String = ""
)
