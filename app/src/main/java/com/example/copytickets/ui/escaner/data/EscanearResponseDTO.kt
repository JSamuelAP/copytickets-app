package com.example.copytickets.ui.escaner.data

import com.google.gson.annotations.SerializedName

data class EscanearResponseDTO(
    @SerializedName("message") val message: String,
    @SerializedName("num_entradas") val numEntradas: Int?
)
