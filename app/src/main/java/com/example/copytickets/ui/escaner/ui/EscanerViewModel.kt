package com.example.copytickets.ui.escaner.ui

import androidx.lifecycle.ViewModel
import com.example.copytickets.ui.escaner.data.EscanearResponseDTO
import kotlinx.coroutines.delay

class EscanerViewModel : ViewModel() {
    suspend fun onScan(): EscanearResponseDTO {
        delay(500)
        return EscanearResponseDTO("ACEPTADO", 3)
    }
}