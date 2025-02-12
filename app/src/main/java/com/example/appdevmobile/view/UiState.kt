package com.example.appdevmobile.view

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>() // Estado inicial, sem interação
    data object Loading : UiState<Nothing>() // Enquanto a requisição está sendo processada
    data class Success<out T>(val data: T) : UiState<T>() // Dados de sucesso
    data class Error(val message: String, val errorCode: Int) : UiState<Nothing>() // Erro com mensagem
}