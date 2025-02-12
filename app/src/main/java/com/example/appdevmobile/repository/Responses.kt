package com.example.appdevmobile.repository

data class LoginResponse(
    val sucesso: Int,
    val erro: String? = null,
    val cod_erro: Int? = null
)

data class CadastroResponse(
    val sucesso: Int,
    val erro: String? = null,
    val cod_erro: Int? = null
)