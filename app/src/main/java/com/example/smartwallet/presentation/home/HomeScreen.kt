package com.example.smartwallet.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartwallet.domain.model.Transaction

@Composable
fun HomeScreen(
    onViewReceipts: () -> Unit,
    onAddReceipt: () -> Unit,
    onOpenP2P: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WalletBalanceCard(balance = state.user?.balance ?: 0.0)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(modifier = Modifier.weight(1f), onClick = onAddReceipt) {
                        Text("Scan / Add Receipt")
                    }
                    Button(modifier = Modifier.weight(1f), onClick = onViewReceipts) {
                        Text("View Receipts")
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(modifier = Modifier.weight(1f), onClick = onOpenP2P) {
                        Text("Send Money")
                    }
                    Button(modifier = Modifier.weight(1f), onClick = onOpenP2P) {
                        Text("Request Money")
                    }
                }
            }
            item {
                SpendingSummaryCard(total = state.totalMonthlySpending, breakdown = state.categoryBreakdown)
            }
            item {
                Text(
                    text = "Latest Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(state.recentTransactions) { transaction ->
                TransactionRow(transaction)
            }
        }
    }
}

@Composable
private fun WalletBalanceCard(balance: Double) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Wallet Balance", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$" + String.format("%.2f", balance),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SpendingSummaryCard(total: Double, breakdown: Map<String, Double>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "This Month", style = MaterialTheme.typography.titleMedium)
            Text(text = "$" + String.format("%.2f", total), style = MaterialTheme.typography.headlineSmall)
            breakdown.forEach { (category, amount) ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(category)
                    Text("$" + String.format("%.2f", amount))
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.description ?: "Transfer", fontWeight = FontWeight.SemiBold)
                Text(transaction.status.name)
            }
            Text("$" + String.format("%.2f", transaction.amount))
        }
    }
}
