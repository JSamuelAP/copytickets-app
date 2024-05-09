package com.example.copytickets.data

import android.content.Context
import com.example.copytickets.ui.escaneos.data.EscaneosRepository
import com.example.copytickets.ui.escaneos.data.OfflineEscaneosRepository

interface AppContainer {
    val escaneosRepository: EscaneosRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val escaneosRepository: EscaneosRepository by lazy {
        OfflineEscaneosRepository(AppDatabase.getDatabase(context).escaneoDao())
    }
}