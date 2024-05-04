package com.example.copytickets.ui.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.copytickets.navigation.AppScreens
import kotlinx.coroutines.delay

class LoginViewModel(val navController: NavController) : ViewModel() {
    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoadng: LiveData<Boolean> = _isLoading

    fun onLoginChanged(usuario: String, password: String) {
        _usuario.value = usuario
        _password.value = password
        _loginEnabled.value = isValidUsuario(usuario) && isValidPassword(password)
    }

    private fun isValidUsuario(usuario: String): Boolean = usuario.length > 0
    private fun isValidPassword(password: String): Boolean = password.length > 0

    suspend fun onLogin() {
        _isLoading.value = true
        delay(3000)
        navController.navigate(route = AppScreens.ScannerScreen.route)
        _isLoading.value = false
    }
}