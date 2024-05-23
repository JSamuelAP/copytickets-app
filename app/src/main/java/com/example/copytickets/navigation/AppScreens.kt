package com.example.copytickets.navigation

/**
 * Definiciones de las pantallas de la app.
 * Cada pantalla se indica como un objeto y su ruta asociada.
 */
sealed class AppScreens(val route: String) {
    data object LoginScreen: AppScreens("login")
    data object ScannerScreen: AppScreens("scanner")
    data object LogsScreen: AppScreens("logs")
}