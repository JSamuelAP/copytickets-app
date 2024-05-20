package com.example.copytickets.screens

import android.Manifest
import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import com.example.copytickets.ui.login.data.DataStoreRepository
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.copytickets.navigation.AppScreens
import com.example.copytickets.ui.components.BottomBar
import com.example.copytickets.ui.escaneos.ReconocimientoQR
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    navController: NavController,
    repository: DataStoreRepository
){
    //Recordar si se han dado permisos de camara anteriormente
    val estadoPermisosCamara: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    //Launcher de la solicitud con comportamiento acorde a la decisión del usuario
    val solicitudLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){  permisoAceptado ->
        if(permisoAceptado){
            navController.navigate(route = AppScreens.ScannerScreen.route)
        } else {
            navController.navigate(route = AppScreens.LogsScreen.route)
        }
    }

    var permisosSolicitados = remember { mutableStateOf(false) }
    if(estadoPermisosCamara.status.isGranted){ //Permisos activos
        CamaraScreen(navController)
    } else {
        LaunchedEffect(permisosSolicitados) {
            //Mostrar solicitud de permisos
            solicitudLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun CamaraScreen(navController: NavController){
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    CamaraContent(navController, lifecycleOwner, cameraController)
}

@Composable
private fun CamaraContent(
    navController: NavController,
    lifecycleOwner: LifecycleOwner? = null,
    cameraController: LifecycleCameraController? = null,
) {
    var deteccion: String by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //Callback tras una lectura de QR exitosa
    //TODO: registrar lectura en la base de datos
    fun onSuccessfulDetection(contenidoQR: String) {
        deteccion = contenidoQR
        scope.launch {
            //Cerrar mensaje previo si existe
            snackbarHostState.currentSnackbarData?.dismiss()
            //Mostrar nuevo mensaje
            val resultado = snackbarHostState
                .showSnackbar(
                    message = deteccion,
                    actionLabel = "Ver",
                    duration = SnackbarDuration.Indefinite
                )
            when(resultado){
                SnackbarResult.ActionPerformed -> { //Al pulsar "Ver"
                    //TODO: Mostrar dialogo con info del qr?
                }
                SnackbarResult.Dismissed -> {
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues: PaddingValues ->
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEDEAEB))
                    .padding(20.dp),
                text = "Coloque el código QR dentro del recuadro.",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF898889)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(300.dp)
                ) {
                    CamaraView(lifecycleOwner, cameraController, ::onSuccessfulDetection)
                }
            }
        }
    }
}

@Composable
fun CamaraView(
    lifecycleOwner: LifecycleOwner? = null,
    cameraController: LifecycleCameraController? = null,
    onSuccessfulDetection: (String) -> Unit){
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }.also { previewView ->
                if (cameraController != null && lifecycleOwner != null) {
                    iniciarReconocimiento(
                        context = context,
                        cameraController = cameraController,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        onDetectedTextUpdated = onSuccessfulDetection
                    )
                }
            }
        }
    )
}

private fun iniciarReconocimiento(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit
) {
    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        ReconocimientoQR(onSuccessfulDetection = onDetectedTextUpdated)
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}

@Preview(showBackground = true)
@Composable
private fun ScannerScreenPreview() {
    CamaraContent(navController = rememberNavController())
}