package com.example.copytickets.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.navigation.AppScreens

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        actions = {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                navController.navigate(route = AppScreens.LoginScreen.route) {
                    // Al hacer logout no se puede volver a las pantallas anteriores con el botón
                    // atras. Para entrar se debe volver a iniciar sesión
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
            }
            Spacer(modifier = Modifier.weight(2f))
            IconButton(onClick = {
                navController.navigate(route = AppScreens.ScannerScreen.route)
            }) {
                Icon(Icons.Filled.Home, contentDescription = "Go to scanner screen")
            }
            Spacer(modifier = Modifier.weight(2f))
            IconButton(onClick = {
                navController.navigate(route = AppScreens.LogsScreen.route)
            }) {
                Icon(Icons.Filled.List, contentDescription = "Go to scanner logs")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    )
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}
