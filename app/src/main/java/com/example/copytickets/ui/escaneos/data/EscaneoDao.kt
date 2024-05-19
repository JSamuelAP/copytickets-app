package com.example.copytickets.ui.escaneos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para el acceso a los datos de la tabla Escaneos.
 * Define solo las operaciones que necesitará realizar la app.
 */
@Dao
interface EscaneoDao {
    @Query("SELECT * FROM escaneos WHERE escaner = :escaner")
    fun getAllLogs(escaner: String): Flow<List<Escaneo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(escaneo: Escaneo)
}