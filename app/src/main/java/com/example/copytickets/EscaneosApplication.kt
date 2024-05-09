package com.example.copytickets

import android.app.Application
import com.example.copytickets.data.AppContainer
import com.example.copytickets.data.AppDataContainer

class EscaneosApplication : Application() {
    /**
     * Instancia usara por el resto de clases para obtener dependencias
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}