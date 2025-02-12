package com.example.appdevmobile.view.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdevmobile.core.Config
import com.example.appdevmobile.repository.ApiService
import com.example.appdevmobile.repository.createApiService
import com.example.appdevmobile.repository.createRetrofit
import com.example.appdevmobile.view.UiState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val apiService: ApiService = createApiService(createRetrofit())

    var uiState by mutableStateOf<UiState<Boolean>>(UiState.Idle)
        private set



    fun login(usuario: String, senha: String) {
        viewModelScope.launch {
            uiState = UiState.Loading

            try {

                val response = apiService.login(Config.auth(login = usuario, password = senha))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.sucesso == 1) {
                        uiState = UiState.Success(true)
                    } else {
                        uiState = UiState.Error(body?.erro ?: "Erro desconhecido", body?.cod_erro ?: -1)
                    }
                } else {
                    uiState = UiState.Error("Falha na comunicação com o servidor", -1)
                }

            } catch (e: Exception) {
                uiState = UiState.Error("Erro inesperado: ${e.message}", -1)
            }
        }
    }
}