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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.copytickets.navigation.AppScreens
import com.example.copytickets.ui.components.BottomBar
import com.example.copytickets.ui.components.MyAlertDialog
import com.example.copytickets.ui.escaneos.ui.EscaneoViewModel
import com.example.copytickets.ui.escaner.ReconocimientoQR
import com.example.copytickets.ui.escaner.ui.EscanerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    viewModel: EscanerViewModel,
    db: EscaneoViewModel,
    navController: NavController,
) {
    //Recordar si se han dado permisos de camara anteriormente
    val estadoPermisosCamara: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    //Launcher de la solicitud con comportamiento acorde a la decisión del usuario
    val solicitudLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permisoAceptado ->
        if (permisoAceptado) {
            navController.navigate(route = AppScreens.ScannerScreen.route)
        } else {
            navController.navigate(route = AppScreens.LogsScreen.route)
        }
    }

    val permisosSolicitados = remember { mutableStateOf(false) }
    if (estadoPermisosCamara.status.isGranted) { //Permisos activos
        CamaraScreen(navController, viewModel, db)
    } else {
        LaunchedEffect(permisosSolicitados) {
            //Mostrar solicitud de permisos
            solicitudLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun CamaraScreen(navController: NavController, viewModel: EscanerViewModel, db: EscaneoViewModel) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }

    CamaraContent(navController, lifecycleOwner, cameraController, viewModel, db)
}

@Composable
private fun CamaraContent(
    navController: NavController,
    lifecycleOwner: LifecycleOwner? = null,
    cameraController: LifecycleCameraController? = null,
    viewModel: EscanerViewModel,
    db: EscaneoViewModel
) {
    var deteccion: String by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var show by rememberSaveable { mutableStateOf(false) }
    var block by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var text by rememberSaveable { mutableStateOf("") }

    //Callback tras una lectura de QR exitosa
    fun onSuccessfulDetection(contenidoQR: String) {
        if (block) return
        block = true
        deteccion = contenidoQR
        if (isValidID(deteccion)) {
            scope.launch {
                val res = viewModel.onScan(deteccion)
                title = res.message
                text = when (title) {
                    "ACEPTADO" -> "Acceso valido para ${res.numEntradas} personas"
                    "DUPLICADO" -> "Este boleto ya fue escaneado"
                    "RECHAZADO" -> "No se pudo reconocer el QR"
                    else -> "Hubo un error al escanear el QR, contactese con el administrador"
                }
                show = true
                db.saveLog(title)
            }

        } else {
            scope.launch {
                title = "RECHAZADO"
                text = "QR no válido para CopyTickets"
                show = true
                db.saveLog(title)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) }
    ) { paddingValues: PaddingValues ->
        MyAlertDialog(
            title = title,
            text = text,
            show = show,
            onDismiss = {
                show = false
                block = false
            }
        )
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
    onSuccessfulDetection: (String) -> Unit
) {
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

fun isValidID(value: String): Boolean {
    return try {
        val intValue = value.toInt()
        intValue > 0
    } catch (e: NumberFormatException) {
        false
    }
}

/*
@Preview(showBackground = true)
@Composable
private fun ScannerScreenPreview() {
    CamaraContent(navController = rememberNavController())
}
*/