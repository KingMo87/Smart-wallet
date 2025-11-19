package com.example.smartwallet.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.User
import com.example.smartwallet.domain.usecase.GetReceiptsUseCase
import com.example.smartwallet.domain.usecase.ObserveCurrentUserUseCase
import com.example.smartwallet.domain.usecase.ObserveTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getReceiptsUseCase: GetReceiptsUseCase,
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeTransactionsUseCase: ObserveTransactionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init {
        viewModelScope.launch {
            combine(
                observeCurrentUserUseCase(),
                getReceiptsUseCase(),
                observeTransactionsUseCase()
            ) { user, receipts, transactions ->
                val currentMonth = receipts.filter { it.date >= monthStart() }
                val totalSpent = currentMonth.sumOf { it.amount }
                val categoryBreakdown = currentMonth.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
                HomeUiState(
                    isLoading = false,
                    user = user,
                    totalMonthlySpending = totalSpent,
                    categoryBreakdown = categoryBreakdown,
                    recentTransactions = transactions.take(3)
                )
            }.collect { uiState ->
                _state.value = uiState
            }
        }
    }

    private fun monthStart(): Long {
        val now = java.time.ZonedDateTime.now()
        return now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            .toInstant().toEpochMilli()
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val totalMonthlySpending: Double = 0.0,
    val categoryBreakdown: Map<String, Double> = emptyMap(),
    val recentTransactions: List<Transaction> = emptyList()
)
