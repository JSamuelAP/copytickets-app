package com.example.copytickets.ui.login.data

import com.google.gson.annotations.SerializedName

data class LoginBodyDTO(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
