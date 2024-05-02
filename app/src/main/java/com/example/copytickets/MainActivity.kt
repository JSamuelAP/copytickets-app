package com.example.copytickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.copytickets.navigation.AppNavigation
import com.example.copytickets.ui.theme.CopyTicketsTheme

/**
 * Instituto Tecnológico Nacional de México Campus León
 * Desarrollo de aplicaciones móviles 8:45-10:25 Febrero-Mayo 2024
 * Docente: Suarez y Gómez Luis Fernando
 * Alumnos:
 *      - Aldana Pérez José Samuel
 *      - Arellano Christian ??
 *      - Eduardo Pedroza Prieto ??
 *
 * Proyecto final: CopyTickets
 * App que permite escanear códigos de boletos QR para dar acceso a eventos.
 * Los códigos QR son generados por el proyecto CopyTickets de la asignatura de Programación web.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CopyTicketsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CopyTicketsTheme {
        AppNavigation()
    }
}