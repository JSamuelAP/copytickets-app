package com.example.copytickets.ui.escaner.data

import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Path

interface EscanerApiService {
    @PUT("escaner/escanear/{id}")
    suspend fun escanear(@Path("id") id: String): Response<EscanearResponseDTO>
}