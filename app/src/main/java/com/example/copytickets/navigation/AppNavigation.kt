package com.example.copytickets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.screens.LoginScreen
import com.example.copytickets.screens.LogsScreen
import com.example.copytickets.screens.ScannerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.ScannerScreen.route) {
            ScannerScreen(navController)
        }
        composable(route = AppScreens.LogsScreen.route) {
            LogsScreen(navController)
        }
    }
}