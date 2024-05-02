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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.ui.components.BottomBar

@Composable
fun LogsScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        LogsContent(navController, innerPadding)
    }
}

@Composable
fun LogsContent(
    navController: NavController,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registros")
    }
}

@Preview(showBackground = true)
@Composable
fun LogsScreenPreview() {
    LogsScreen(navController = rememberNavController())
}