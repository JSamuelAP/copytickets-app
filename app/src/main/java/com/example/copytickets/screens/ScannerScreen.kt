package com.example.copytickets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.copytickets.ui.components.BottomBar
import com.example.copytickets.ui.login.data.DataStoreRepository

@Composable
fun ScannerScreen(
    navController: NavController,
    repository: DataStoreRepository
) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        ScannerContent(innerPadding)
    }
}

@Composable
fun ScannerContent(
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Escanear QR")
    }
}