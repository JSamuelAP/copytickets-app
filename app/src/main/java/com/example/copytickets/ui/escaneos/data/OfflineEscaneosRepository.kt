package com.example.copytickets.ui.escaneos.data

import kotlinx.coroutines.flow.Flow

/**
 * Clase que implementa el repositorio con EscaneoDao como fuente de datos.
 */
class OfflineEscaneosRepository(private val escaneoDao: EscaneoDao) : EscaneosRepository {
    override fun getAllLogsStream(escaner: Int): Flow<List<Escaneo>> =
        escaneoDao.getAllLogs(escaner)

    override suspend fun insertLog(log: Escaneo) = escaneoDao.insert(log)
}