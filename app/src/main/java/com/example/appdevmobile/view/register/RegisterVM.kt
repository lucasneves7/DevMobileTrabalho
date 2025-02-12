package com.example.appdevmobile.view.register

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdevmobile.core.toDateFormat
import com.example.appdevmobile.repository.ApiService
import com.example.appdevmobile.repository.CadastroResponse
import com.example.appdevmobile.repository.createApiService
import com.example.appdevmobile.repository.createRetrofit
import com.example.appdevmobile.view.UiState
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegisterVM : ViewModel() {

    private val apiService: ApiService = createApiService(createRetrofit())

    var uiState by mutableStateOf<UiState<CadastroResponse>>(UiState.Idle)
        private set

    var imageState by mutableStateOf<File?>(null)

    fun onImageSelected(uri: Uri, context: Context) {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            uri,
            "r",
            null
        )
        val file = File(
            context.cacheDir,
            context.contentResolver.getFileName(uri)
        )
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        imageState = file
    }

    fun onImageSelected(file: File){
        imageState = file
    }


    fun cadastraUsuario(
        login: String,
        senha: String,
        nome: String,
        cidade: String,
        dataNascimento: Long,
    ) {
        viewModelScope.launch {
            uiState = UiState.Loading

            try {
                val file = imageState ?: throw Exception("Selecione a imagem")
                val fotoRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val fotoPart = MultipartBody.Part.createFormData("foto", file.name, fotoRequestBody)
//                val requestFile: RequestBody = uri?.asRequestBody("multipart/form-data".toMediaTypeOrNull())

//                val requestFile: RequestBody = imageState?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//                    ?: throw Exception("Selecione a imagem")
//
//                val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

                val _dataNascimento = dataNascimento.toDateFormat("YYYY-MM-DD")
                val loginRequestBody = login.toRequestBody("text/plain".toMediaTypeOrNull())
                val senhaRequestBody = senha.toRequestBody("text/plain".toMediaTypeOrNull())
                val nomeRequestBody = nome.toRequestBody("text/plain".toMediaTypeOrNull())
                val cidadeRequestBody = cidade.toRequestBody("text/plain".toMediaTypeOrNull())
                val dataNascimentoRequestBody = _dataNascimento.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.cadastraUsuario(
                    loginRequestBody,
                    senhaRequestBody,
                    nomeRequestBody,
                    cidadeRequestBody,
                    dataNascimentoRequestBody,
                    fotoPart
                )
                Log.i("TAG", "NAO DEU ERRO" + response.body().toString())

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.sucesso == 1) {
                        uiState = UiState.Success(body)
                    } else {
                        Log.i("TAG", body?.erro ?: "erro")
                        uiState = UiState.Error(body?.erro ?: "Erro desconhecido", body?.cod_erro ?: -1)
                    }
                } else {
                    Log.i("TAG",  "erro 2")
                    uiState = UiState.Error("Falha na comunicação com o servidor", -1)
                }

            } catch (e: Exception) {
                Log.i("TAG", e.message ?: "erro")
                uiState = UiState.Error( e.message ?: "Erro desconhecido", -1)
            }
        }
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(
            uri, null, null,
            null, null
        )
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }

    private fun formatDate(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE)  // "yyyy-MM-dd"
    }
}