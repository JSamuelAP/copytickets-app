package com.example.copytickets.ui.escaner

import android.media.Image
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ReconocimientoQR(private val onSuccessfulDetection: (String) -> Unit) : ImageAnalysis.Analyzer {

    //Tiempo de espera entre intentos de escaneo
    companion object {
        const val ESPERA_MS = 1_000L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    //Solo reconocer códigos QR
    private val opciones = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val reconocedorQR: BarcodeScanner = BarcodeScanning.getClient(opciones)

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            //Empezar a capturar la cámara
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            //Capturar frame a analizar
            val inputImage: InputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            //Intento de procesamiento del código QR
            suspendCoroutine { continuation ->
                reconocedorQR.process(inputImage)
                    .addOnSuccessListener {barcodes: List<Barcode> ->
                        //Considerar solo el primer código reconocido cuando hay varios en pantalla
                        if(barcodes.isNotEmpty()){
                            val resultado: String? = barcodes[0].rawValue
                            if (resultado!!.isNotBlank()) {
                                onSuccessfulDetection(resultado) //Callback definido en ScannerScreen
                            }
                        }
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }
            delay(ESPERA_MS) //Espera antes del siguiente intento
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }
}