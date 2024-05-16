package com.example.copytickets.ui.login.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.copytickets.navigation.AppScreens
import com.example.copytickets.ui.login.data.LoginBodyDTO
import com.example.copytickets.ui.login.data.LoginResponseDTO
import com.example.copytickets.ui.login.data.RetrofitHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val navController: NavController) : ViewModel() {
    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val loginRequestLiveData = MutableLiveData<Boolean>()
    val resMessage = mutableStateOf(value = "")

    fun onLoginChanged(usuario: String, password: String) {
        _usuario.value = usuario
        _password.value = password
        _loginEnabled.value = isValidUsuario(usuario) && isValidPassword(password)
    }

    private fun isValidUsuario(usuario: String): Boolean = usuario.isNotEmpty()
    private fun isValidPassword(password: String): Boolean = password.isNotEmpty()

    suspend fun onLogin() {
        viewModelScope.launch(IO) {
            withContext(Main) {
                _isLoading.value = true
            }
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getLogin(
                    LoginBodyDTO(_usuario.value!!, _password.value!!
                ))

                if(responseService.isSuccessful) {
                    responseService.body()?.let { res ->
                        Log.d("Logging", "token: ${res.accessToken}")
                        resMessage.value = res.message
                    }
                    withContext(Main) {
                        navController.navigate(route = AppScreens.ScannerScreen.route)
                    }
                } else {
                    responseService.errorBody()?.let { error ->
                        val res = Gson().fromJson(error.string(), LoginResponseDTO::class.java)
                        resMessage.value = res.message
                        Log.d("Logging", "error: ${res.message}")
                    }
                }
                loginRequestLiveData.postValue(responseService.isSuccessful)
            } catch (e: Exception) {
                Log.d("Loggin", "Error de autentication", e)
            } finally {
                withContext(Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}