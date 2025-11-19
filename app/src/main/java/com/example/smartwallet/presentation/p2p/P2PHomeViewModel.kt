package com.example.smartwallet.presentation.p2p

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.User
import com.example.smartwallet.domain.usecase.GetContactsUseCase
import com.example.smartwallet.domain.usecase.ObserveCurrentUserUseCase
import com.example.smartwallet.domain.usecase.ObserveTransactionsUseCase
import com.example.smartwallet.domain.usecase.RequestMoneyUseCase
import com.example.smartwallet.domain.usecase.SendMoneyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class P2PHomeViewModel @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val observeTransactionsUseCase: ObserveTransactionsUseCase,
    private val getContactsUseCase: GetContactsUseCase,
    private val sendMoneyUseCase: SendMoneyUseCase,
    private val requestMoneyUseCase: RequestMoneyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(P2PHomeUiState())
    val state: StateFlow<P2PHomeUiState> = _state

    init {
        viewModelScope.launch {
            observeCurrentUserUseCase().collectLatest { user ->
                _state.update { it.copy(currentUser = user) }
            }
        }
        viewModelScope.launch {
            observeTransactionsUseCase().collectLatest { transactions ->
                _state.update { it.copy(transactions = transactions) }
            }
        }
        viewModelScope.launch {
            val contacts = getContactsUseCase()
            _state.update { it.copy(contacts = contacts) }
        }
    }

    fun sendMoney(userId: String, amount: Double, currency: String, note: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            sendMoneyUseCase(userId, amount, currency, note)
            _state.update { it.copy(isProcessing = false) }
        }
    }

    fun requestMoney(userId: String, amount: Double, currency: String, note: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            requestMoneyUseCase(userId, amount, currency, note)
            _state.update { it.copy(isProcessing = false) }
        }
    }
}

data class P2PHomeUiState(
    val currentUser: User? = null,
    val contacts: List<User> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val isProcessing: Boolean = false
)
