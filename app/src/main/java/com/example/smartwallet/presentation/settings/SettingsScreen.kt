package com.example.smartwallet.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    val (biometrics, setBiometrics) = remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", modifier = Modifier.padding(bottom = 12.dp))
        Text("Biometric unlock")
        Switch(checked = biometrics, onCheckedChange = setBiometrics)
    }
}
