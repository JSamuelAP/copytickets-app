package com.example.copytickets.ui.escaner.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.copytickets.data.RetrofitHelper
import com.example.copytickets.ui.escaner.data.EscanearResponseDTO
import com.example.copytickets.ui.login.data.DataStoreRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EscanerViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _token = MutableLiveData<String?>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.getScannerData().collect {
                withContext(Dispatchers.Main) {
                    _token.value = it.token
                }
            }
        }
    }

    suspend fun onScan(id: String): EscanearResponseDTO {
        try {
            val escanerService = RetrofitHelper.getEscanerService(_token.value.orEmpty())
            val responseService = escanerService.escanear(id)

            if(responseService.isSuccessful) {
                responseService.body()?.let { res->
                    Log.d("Scanner", "${res.message} - ${res.numEntradas}")
                    return res
                }
            } else {
                responseService.errorBody()?.let { error ->
                    val res = Gson().fromJson(error.string(), EscanearResponseDTO::class.java)
                    Log.d("Scanner", res.message)
                    return res
                }
            }
        } catch(e: Exception) {
            return EscanearResponseDTO("ERROR", null)
        }
    }
}