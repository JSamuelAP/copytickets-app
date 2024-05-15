package com.example.copytickets.ui.login.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("escaner/login")
    suspend fun getLogin(@Body loginBodyDTO: LoginBodyDTO): Response<LoginResponseDTO>
}