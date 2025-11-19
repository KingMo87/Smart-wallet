package com.example.smartwallet.presentation.receipts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartwallet.domain.model.Receipt

@Composable
fun ReceiptListScreen(
    viewModel: ReceiptListViewModel,
    onAddReceipt: () -> Unit,
    onReceiptSelected: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.searchQuery,
            onValueChange = viewModel::onSearch,
            label = { Text("Search receipts") }
        )
        TextButton(onClick = onAddReceipt) {
            Text("Add Receipt")
        }
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.filteredReceipts) { receipt ->
                    ReceiptRow(receipt = receipt, onClick = { onReceiptSelected(receipt.id) })
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(receipt: Receipt, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(receipt.merchantName, fontWeight = FontWeight.SemiBold)
                Text("$" + String.format("%.2f", receipt.amount))
            }
            AssistChip(onClick = {}, label = { Text(receipt.category) }, enabled = false)
            Text(java.text.SimpleDateFormat("MMM dd, yyyy").format(java.util.Date(receipt.date)))
        }
    }
}
