package com.example.copytickets.ui.login.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.copytickets.navigation.AppScreens
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val navController: NavController
) : ViewModel() {
    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
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
                delay(1000)
                navController.navigate(route = AppScreens.ScannerScreen.route) {
                    // Al hacer login, no se puede volver a la pantalla de login con el
                    // boton atras, solo se podrá con el boton logout
                    popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                }
                _isLoading.value = false
            }
        }
    }
}