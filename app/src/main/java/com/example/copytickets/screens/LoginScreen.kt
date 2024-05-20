package com.example.copytickets.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.R
import com.example.copytickets.ui.login.ui.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Scaffold { innerPadding ->
        LoginContent(viewModel, innerPadding)
    }
}

@Composable
fun LoginContent(
    viewModel: LoginViewModel,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val usuario: String by viewModel.usuario.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnabled: Boolean by viewModel.loginEnabled.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val resMessage = viewModel.resMessage.value

    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Box(modifier.fillMaxSize()) {
            //TODO: Crashea la app al realizar el indicador de progreso
            //CircularProgressIndicator(modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(modifier)
            Spacer(modifier.height(24.dp))
            UserField(usuario, { viewModel.onLoginChanged(it, password) }, modifier)
            Spacer(modifier.height(16.dp))
            PasswordField(password, { viewModel.onLoginChanged(usuario, it) }, modifier)
            Spacer(modifier.height(16.dp))
            LoginButton(
                loginEnabled,
                { coroutineScope.launch { viewModel.onLogin() } },
                modifier
            )
            if (resMessage.isNotBlank()) {
                Spacer(modifier.height(8.dp))
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .width(300.dp)
                ) {
                    Box(modifier.padding(8.dp).width(300.dp)) {
                        Text(
                            text = resMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "CopyTickets logo",
        modifier = modifier.size(300.dp)
    )
    Text("CopyTickets", fontSize = 32.sp)
}

@Composable
fun UserField(
    usuario: String,
    onTextFieldChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = usuario,
        onValueChange = { onTextFieldChange(it) },
        label = { Text("Usuario") },
        singleLine = true,
        modifier = modifier.width(300.dp)
    )
}

@Composable
fun PasswordField(
    password: String,
    onTextFieldChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = password,
        onValueChange = { onTextFieldChange(it) },
        label = { Text("Contraseña") },
        modifier = modifier.width(300.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true
    )
}

@Composable
fun LoginButton(
    loginEnabled: Boolean,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onLogin,
        enabled = loginEnabled,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier.width(300.dp)
    ) {
        Text("Iniciar sesión")
    }
}