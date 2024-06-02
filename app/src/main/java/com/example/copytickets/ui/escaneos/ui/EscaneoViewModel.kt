package com.example.copytickets.ui.escaneos.ui

import androidx.lifecycle.ViewModel
import com.example.copytickets.ui.escaneos.data.Escaneo
import com.example.copytickets.ui.escaneos.data.EscaneosRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EscaneoViewModel(
    private val escaneosRepository: EscaneosRepository
) : ViewModel() {

    suspend fun saveLog(resultado: String) {
        val fechaActual = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val horaActual = LocalTime.now()
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")

        val escaneo = Escaneo(
            escaner = "1",
            fecha = fechaActual.format(formatterDate),
            hora = horaActual.format(formatterTime),
            resultado = resultado
        )

        escaneosRepository.insertLog(escaneo)
    }

    fun getLogs(): Flow<List<Escaneo>> = escaneosRepository.getAllLogsStream("1")
}
