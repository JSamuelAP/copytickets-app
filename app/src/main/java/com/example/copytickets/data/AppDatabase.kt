package com.example.copytickets.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.copytickets.ui.escaneos.data.Escaneo
import com.example.copytickets.ui.escaneos.data.EscaneoDao

/**
 * Clase que representa la base de datos local
 */
@Database(entities = [Escaneo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun escaneoDao(): EscaneoDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "escaneos_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}