package com.example.appdevmobile.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("login.php")
    suspend fun login(
        @Header("Authorization") auth: String
    ): Response<LoginResponse>

    @Multipart
    @POST("cadastra_usuario.php")
    suspend fun cadastraUsuario(
        @Part("login") login: RequestBody,
        @Part("senha") senha: RequestBody,
        @Part("nome") nome: RequestBody,
        @Part("cidade") cidade: RequestBody,
        @Part("data_nascimento") dataNascimento: RequestBody,
        @Part foto: MultipartBody.Part // Imagem como Multipart
    ): Response<CadastroResponse>
}