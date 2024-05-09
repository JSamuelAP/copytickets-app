package com.example.copytickets.ui.escaneos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase de datos que representa la entidad Escaneo de la BD
 */
@Entity(tableName = "escaneos")
data class Escaneo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "escaner") val escaner: Int,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "hora") val hora: String,
    @ColumnInfo(name = "resultado") val resultado: String
)
