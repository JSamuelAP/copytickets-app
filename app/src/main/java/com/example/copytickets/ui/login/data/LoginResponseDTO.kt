package com.example.copytickets.ui.login.data

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("message") val message: String,
    @SerializedName("escaner") val escaner: EscanerResponse?,
    @SerializedName("access_token") val accessToken: String?
)

data class EscanerResponse(
    @SerializedName("id") val id: String,
    @SerializedName("evento_id") val eventoID: String,
    @SerializedName("usuario") val usuario: String
)