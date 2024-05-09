package com.example.copytickets.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.EscaneosApplication
import com.example.copytickets.ui.escaneos.data.Escaneo
import com.example.copytickets.ui.components.BottomBar
import com.example.copytickets.ui.escaneos.ui.EscaneoViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogsScreen(viewModel: EscaneoViewModel, navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        LogsContent(viewModel, innerPadding)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogsContent(
    viewModel: EscaneoViewModel,
    innerPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val data = listOf(
            Escaneo(1, 1, "26/04/2024", "21:00", "Aceptado"),
            Escaneo(2, 1, "26/04/2024", "21:05", "Error"),
            Escaneo(3, 1, "26/04/2024", "21:30", "Duplicado"),
            Escaneo(4, 1, "26/04/2024", "22:10", "Rechazado")
        )
        Text(
            text = "Escaneos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 48.dp, start = 32.dp)
                .align(Alignment.Start)
        )
        // TODO: Eliminar este boton, solo es temporal para simular escaneos
        Button(onClick = {
            coroutineScope.launch {
                viewModel.saveLog(1, "Aceptado")
            }
        }) {
            Text("Agregar escaneo")
        }
        Table(escaneos = data)
    }
}

@Composable
fun Table(
    escaneos: List<Escaneo>,
    modifier: Modifier = Modifier
) {
    val column1Weight = .4f
    val column2Weight = .4f
    val column3Weight = .2f

    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 32.dp, end = 32.dp)
    ) {
        item {
            Row {
                TableCell(text = "Resultado", weight = column1Weight, heading = true)
                TableCell(text = "Fecha", weight = column2Weight, heading = true)
                TableCell(text = "Hora", weight = column3Weight, heading = true)
            }
            Divider(
                color = Color.LightGray, modifier = modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
        }
        items(escaneos) {
            val (_, _, fecha, hora, resultado) = it
            Row(modifier.fillMaxWidth()) {
                TableCell(text = resultado, weight = column1Weight)
                TableCell(text = fecha, weight = column2Weight)
                TableCell(text = hora, weight = column3Weight)
            }
            Divider(
                color = Color.LightGray, modifier = modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    heading: Boolean = false
) {
    Text(
        text = text,
        fontWeight = if (heading) FontWeight.Bold else FontWeight.Normal,
        color = when (text) {
            "Aceptado" -> Color.Green
            "Error", "Duplicado", "Rechazado" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurface
        },
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun LogsScreenPreview() {
    val application = LocalContext.current.applicationContext as EscaneosApplication
    LogsScreen(
        EscaneoViewModel(application.container.escaneosRepository),
        navController = rememberNavController()
    )
}