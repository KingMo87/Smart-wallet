package com.example.smartwallet.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartwallet.presentation.home.HomeScreen
import com.example.smartwallet.presentation.p2p.P2PNavigation
import com.example.smartwallet.presentation.receipts.ReceiptDetailScreen
import com.example.smartwallet.presentation.receipts.ReceiptFormScreen
import com.example.smartwallet.presentation.receipts.ReceiptListScreen
import com.example.smartwallet.presentation.receipts.ReceiptListViewModel

object Destinations {
    const val HOME = "home"
    const val RECEIPTS = "receipts/list"
    const val RECEIPT_DETAIL = "receipts/detail/{id}"
    const val RECEIPT_EDIT = "receipts/edit/{id?}"
    const val P2P = "p2p/home"
}

@Composable
fun SmartWalletNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Destinations.HOME) {
        composable(Destinations.HOME) {
            HomeScreen(
                onViewReceipts = { navController.navigate(Destinations.RECEIPTS) },
                onAddReceipt = { navController.navigate("receipts/edit") },
                onOpenP2P = { navController.navigate(Destinations.P2P) }
            )
        }
        composable(Destinations.RECEIPTS) {
            val viewModel = hiltViewModel<ReceiptListViewModel>()
            ReceiptListScreen(
                viewModel = viewModel,
                onAddReceipt = { navController.navigate("receipts/edit") },
                onReceiptSelected = { id -> navController.navigate("receipts/detail/$id") }
            )
        }
        composable("receipts/detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: return@composable
            ReceiptDetailScreen(
                receiptId = id,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("receipts/edit/$id") }
            )
        }
        composable("receipts/edit") {
            ReceiptFormScreen(
                receiptId = null,
                onDone = { navController.popBackStack() }
            )
        }
        composable("receipts/edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            ReceiptFormScreen(
                receiptId = id,
                onDone = { navController.popBackStack() }
            )
        }
        composable(Destinations.P2P) {
            P2PNavigation(onBack = { navController.popBackStack() })
        }
    }
}
