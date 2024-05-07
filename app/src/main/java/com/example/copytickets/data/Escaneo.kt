package com.example.copytickets.data

data class Escaneo(
    val id: Int,
    val escaner: Int,
    val fecha: String,
    val hora: String,
    val resultado: String
)
