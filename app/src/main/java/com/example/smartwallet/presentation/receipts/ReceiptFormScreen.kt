package com.example.smartwallet.presentation.receipts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReceiptFormScreen(
    receiptId: Long?,
    onDone: () -> Unit,
    viewModel: ReceiptFormViewModel = hiltViewModel()
) {
    LaunchedEffect(receiptId) { viewModel.load(receiptId) }
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.merchantName,
            onValueChange = { viewModel.update { copy(merchantName = it) } },
            label = { Text("Merchant") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.amount.takeIf { it != 0.0 }?.toString() ?: "",
            onValueChange = { value ->
                viewModel.update {
                    value.toDoubleOrNull()?.let { copy(amount = it) } ?: this
                }
            },
            label = { Text("Amount") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.category,
            onValueChange = { viewModel.update { copy(category = it) } },
            label = { Text("Category") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.paymentMethod,
            onValueChange = { viewModel.update { copy(paymentMethod = it) } },
            label = { Text("Payment Method") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.notes.orEmpty(),
            onValueChange = { viewModel.update { copy(notes = it.takeIf { it.isNotBlank() }) } },
            label = { Text("Notes") }
        )
        Button(onClick = { viewModel.save(onDone) }) {
            Text("Save")
        }
    }
}
