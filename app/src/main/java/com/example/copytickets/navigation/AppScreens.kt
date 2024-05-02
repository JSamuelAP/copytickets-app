package com.example.copytickets.navigation

/**
 * Definiciones de las pantallas de la app.
 * Cada pantalla se indica como un objeto y su ruta asociada.
 */
sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login")
    object ScannerScreen: AppScreens("scanner")
    object LogsScreen: AppScreens("logs")
}