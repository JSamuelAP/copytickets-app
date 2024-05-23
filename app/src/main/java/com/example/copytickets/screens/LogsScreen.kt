package com.example.copytickets.screens

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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.copytickets.ui.escaneos.data.Escaneo
import com.example.copytickets.ui.components.BottomBar
import com.example.copytickets.ui.escaneos.ui.EscaneoViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun LogsScreen(viewModel: EscaneoViewModel, navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        LogsContent(viewModel, innerPadding)
    }
}

@Composable
fun LogsContent(
    viewModel: EscaneoViewModel,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val escaneos = viewModel.getLogs()
        Text(
            text = "Escaneos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 48.dp, start = 32.dp)
                .align(Alignment.Start)
        )
        Table(escaneos)
    }
}

@Composable
fun Table(
    data: Flow<List<Escaneo>>,
    modifier: Modifier = Modifier
) {
    val escaneos by data.collectAsState(initial = emptyList())
    val column1Weight = .4f
    val column2Weight = .3f
    val column3Weight = .3f

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
            "ACEPTADO" -> Color(53, 151, 57, 255)
            "ERROR", "DUPLICADO", "RECHAZADO" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurface
        },
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
    )
}
