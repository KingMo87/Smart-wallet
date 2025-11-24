package com.example.smartwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.smartwallet.core.SmartWalletTheme
import com.example.smartwallet.presentation.navigation.SmartWalletNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartWalletTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SmartWalletNavHost()
                }
            }
        }
    }
}

