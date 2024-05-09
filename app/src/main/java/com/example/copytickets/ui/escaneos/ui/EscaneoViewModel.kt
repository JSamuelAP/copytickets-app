package com.example.copytickets.ui.escaneos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.copytickets.ui.escaneos.data.Escaneo
import com.example.copytickets.ui.escaneos.data.EscaneosRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EscaneoViewModel(private val escaneosRepository: EscaneosRepository) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveLog(escaner: Int, resultado: String) {
        val fechaActual = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val horaActual = LocalTime.now()
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")

        val escaneo = Escaneo(
            escaner = escaner,
            fecha = fechaActual.format(formatterDate),
            hora = horaActual.format(formatterTime),
            resultado = resultado
        )

        escaneosRepository.insertLog(escaneo)
    }
}
