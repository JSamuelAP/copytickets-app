package com.example.copytickets.ui.escaneos.data

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para el repositorio que define las operaciones que necesita la app.
 */
interface EscaneosRepository {
    /**
     * Obtener todos los escaneos realizados por un usuario escaner.
     */
    fun getAllLogsStream(escaner: Int): Flow<List<Escaneo>>

    /**
     * Insertar un nuevo registro de escaneo en la fuente de datos.
     */
    suspend fun insertLog(log: Escaneo)
}