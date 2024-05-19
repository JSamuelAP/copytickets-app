package com.example.copytickets.ui.escaneos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.copytickets.ui.escaneos.data.Escaneo
import com.example.copytickets.ui.escaneos.data.EscaneosRepository
import com.example.copytickets.ui.login.data.DataStoreRepository
import com.example.copytickets.ui.login.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EscaneoViewModel(
    private val escaneosRepository: EscaneosRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _scannerData = MutableStateFlow(UserData("", ""))
    private val scannerData: StateFlow<UserData> = _scannerData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserData("0", "")
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.getScannerData().collect {
                withContext(Dispatchers.Main) {
                    _scannerData.value = it
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveLog(resultado: String) {
        val fechaActual = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val horaActual = LocalTime.now()
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")

        val escaneo = Escaneo(
            escaner = scannerData.value.id,
            fecha = fechaActual.format(formatterDate),
            hora = horaActual.format(formatterTime),
            resultado = resultado
        )

        escaneosRepository.insertLog(escaneo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLogs(): Flow<List<Escaneo>> = scannerData.flatMapLatest { userData ->
        escaneosRepository.getAllLogsStream(userData.id)
    }
}
