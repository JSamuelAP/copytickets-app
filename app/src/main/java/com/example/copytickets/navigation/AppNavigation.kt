package com.example.copytickets.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.EscaneosApplication
import com.example.copytickets.screens.LoginScreen
import com.example.copytickets.screens.LogsScreen
import com.example.copytickets.screens.ScannerScreen
import com.example.copytickets.ui.escaneos.ui.EscaneoViewModel
import com.example.copytickets.ui.login.ui.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as EscaneosApplication

    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(LoginViewModel(navController))
        }
        composable(route = AppScreens.ScannerScreen.route) {
            ScannerScreen(navController)
        }
        composable(route = AppScreens.LogsScreen.route) {
            LogsScreen(
                EscaneoViewModel(application.container.escaneosRepository),
                navController
            )
        }
    }
}