package com.example.smartwallet.presentation.p2p

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartwallet.domain.model.Transaction
import com.example.smartwallet.domain.model.User

@Composable
fun P2PHomeScreen(
    onBack: () -> Unit,
    viewModel: P2PHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "P2P Center", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Button(onClick = onBack) { Text("Back") }
        }
        state.currentUser?.let {
            Text("Balance: $" + String.format("%.2f", it.balance), style = MaterialTheme.typography.titleMedium)
        }
        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Send", "Request", "History")
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
            }
        }
        when (selectedTab) {
            0 -> SendMoneyTab(state.contacts, state.isProcessing, onSend = { userId, amount, note ->
                viewModel.sendMoney(userId, amount, "USD", note)
            })
            1 -> RequestMoneyTab(state.contacts, state.isProcessing, onRequest = { userId, amount, note ->
                viewModel.requestMoney(userId, amount, "USD", note)
            })
            else -> TransactionHistoryTab(state.transactions)
        }
    }
}

@Composable
private fun SendMoneyTab(contacts: List<User>, isProcessing: Boolean, onSend: (String, Double, String?) -> Unit) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var amount by remember { mutableStateOf(0.0) }
    var note by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContactPicker(contacts = contacts, onSelected = { selectedUser = it })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = amount.takeIf { it != 0.0 }?.toString() ?: "",
            onValueChange = { amount = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Amount") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = note,
            onValueChange = { note = it },
            label = { Text("Description") }
        )
        Button(
            onClick = { selectedUser?.let { onSend(it.id, amount, note.takeIf { it.isNotBlank() }) } },
            enabled = selectedUser != null && amount > 0 && !isProcessing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp), strokeWidth = 2.dp)
            } else {
                Text("Send")
            }
        }
    }
}

@Composable
private fun RequestMoneyTab(contacts: List<User>, isProcessing: Boolean, onRequest: (String, Double, String?) -> Unit) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var amount by remember { mutableStateOf(0.0) }
    var note by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContactPicker(contacts = contacts, onSelected = { selectedUser = it })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = amount.takeIf { it != 0.0 }?.toString() ?: "",
            onValueChange = { amount = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Amount") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = note,
            onValueChange = { note = it },
            label = { Text("Note") }
        )
        Button(
            onClick = { selectedUser?.let { onRequest(it.id, amount, note.takeIf { it.isNotBlank() }) } },
            enabled = selectedUser != null && amount > 0 && !isProcessing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp), strokeWidth = 2.dp)
            } else {
                Text("Request")
            }
        }
    }
}

@Composable
private fun TransactionHistoryTab(transactions: List<Transaction>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(transactions) { transaction ->
            TransactionCard(transaction)
        }
    }
}

@Composable
private fun ContactPicker(contacts: List<User>, onSelected: (User) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 240.dp)
            .padding(vertical = 8.dp)
    ) {
        items(contacts) { contact ->
            Card(onClick = { onSelected(contact) }, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(contact.displayName, fontWeight = FontWeight.SemiBold)
                        Text(contact.phoneNumber)
                    }
                    Text("$" + String.format("%.2f", contact.balance))
                }
            }
        }
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(transaction.description ?: "Transfer", fontWeight = FontWeight.SemiBold)
            Text("Amount: $" + String.format("%.2f", transaction.amount))
            Text("Status: ${transaction.status}")
            Text(java.text.SimpleDateFormat("MMM dd, yyyy HH:mm").format(java.util.Date(transaction.createdAt)))
        }
    }
}
