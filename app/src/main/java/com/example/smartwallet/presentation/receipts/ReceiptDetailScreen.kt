package com.example.smartwallet.presentation.receipts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReceiptDetailScreen(
    receiptId: Long,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: ReceiptDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(receiptId) { viewModel.loadReceipt(receiptId) }
    val state by viewModel.state.collectAsState()
    when (val uiState = state) {
        ReceiptDetailUiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { CircularProgressIndicator() }
        }
        is ReceiptDetailUiState.Error -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(uiState.message)
            }
        }
        is ReceiptDetailUiState.Success -> {
            val receipt = uiState.receipt
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(receipt.merchantName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Amount: $" + String.format("%.2f", receipt.amount))
                Text("Category: ${receipt.category}")
                Text("Date: " + java.text.SimpleDateFormat("MMM dd, yyyy").format(java.util.Date(receipt.date)))
                Text("Payment: ${receipt.paymentMethod}")
                receipt.notes?.let { Text("Notes: $it") }
                Button(onClick = onEdit) { Text("Edit") }
                Button(onClick = {
                    viewModel.delete(receipt) {
                        onBack()
                    }
                }) { Text("Delete") }
            }
        }
    }
}
