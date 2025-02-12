package com.example.appdevmobile.view.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.appdevmobile.core.toDateFormat
import com.example.appdevmobile.ui.theme.AppDevMobileTheme
import com.example.appdevmobile.view.UiState
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Tela de cadastro de usuário da aplicação.
 *
 * Essa Activity exibe um formulário para que o usuário cadastre seu login, senha,
 * nome completo, cidade, data de nascimento e foto de perfil.
 */
class RegisterActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ativa o suporte a tela cheia.

        setContent {
            AppDevMobileTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Cadastro") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, null)
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        item {
                            RegisterScreen { finish() } // Finaliza a atividade após cadastro bem-sucedido
                        }
                    }
                }
            }
        }
    }
}


/**
 * Tela de cadastro de usuário representada por um composable.
 *
 * @param viewModel O ViewModel responsável pelo cadastro do usuário.
 * @param onFinish Callback chamado após um cadastro bem-sucedido.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    viewModel: RegisterVM = koinViewModel(),
    onFinish: () -> Unit
) {

    // Estados para armazenar os dados do usuário
    var login by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var nome by rememberSaveable { mutableStateOf("") }
    var cidade by rememberSaveable { mutableStateOf("") }
    var dataNascimento: Long by rememberSaveable { mutableLongStateOf(0L) }

    val uiState by remember { derivedStateOf { viewModel.uiState } }

    // Observa mudanças no estado da UI e executa ações apropriadas
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) onFinish() // Finaliza após sucesso
    }

    val focusManager = LocalFocusManager.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Exibe o seletor de data de nascimento
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            dataNascimento = millis
                        }
                        showDatePickerDialog = false
                    }) {
                    Text(text = "Escolher data")
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos de entrada para os dados do usuário
        TextField(
            value = login,
            onValueChange = { login = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = { Text("Login (e-mail)") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = { Text("Nome completo") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cidade,
            onValueChange = { cidade = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = { Text("Cidade") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de data de nascimento
        TextField(
            value = dataNascimento.toDateFormat(),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusEvent {
                    if (it.isFocused) {
                        showDatePickerDialog = true // Exibe o seletor de data
                        focusManager.clearFocus(force = true)
                    }
                },
            label = {
                Text("Data de Nascimento")
            },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Componente para selecionar foto de perfil
        ImagePickerScreen(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de cadastro
        Button(
            onClick = {
                viewModel.cadastraUsuario(login, senha, nome, cidade, dataNascimento)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            enabled = uiState !is UiState.Loading
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                }
                else -> {
                    Text("Cadastrar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe mensagens de erro ou sucesso
        when (uiState) {
            is UiState.Error -> {
                Text(
                    text = (uiState as UiState.Error).message,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            is UiState.Success -> {
                Text("Cadastro realizado com sucesso!", color = Color.Green)
            }
            else -> {}
        }
    }
}

/**
 * Componente para permitir que o usuário selecione ou tire uma foto para o avatar.
 *
 * Utiliza as permissões de câmera e armazenamento para capturar ou selecionar uma imagem.
 *
 * @param viewModel O ViewModel responsável por gerenciar o estado da imagem selecionada.
 */
@Composable
private fun ImagePickerScreen(viewModel: RegisterVM) {
    val context = LocalContext.current
    val imageState by remember { derivedStateOf { viewModel.imageState } }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {
            viewModel.onImageSelected(it, context)
        }
    }

    val photoFile = remember {
        createImageFile(context)
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            photoFile
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // A imagem foi salva com sucesso no URI fornecido
            viewModel.onImageSelected(photoFile)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Exibe a imagem selecionada ou um ícone de avatar
        if (imageState != null) {
            Image(painter = rememberImagePainter(imageState), contentDescription = "Selected Image", modifier = Modifier.size(150.dp))
        } else {
            Text("Selecione o avatar.")
            Icon(Icons.Filled.AccountCircle, contentDescription = "Avatar", Modifier.size(150.dp))
        }

        // Botões para tirar foto ou selecionar imagem
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Button(onClick = {
                // Solicitar permissão para câmera
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Abre a câmera
                    cameraLauncher.launch(photoUri)
                } else {
                    // Solicitar permissão para câmera
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.CAMERA),
                        1001
                    )
                }
            }) {
                Text("Tirar Foto")
            }

            Button(onClick = {
                // Abre a galeria de imagens
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text("Selecionar Imagem")
            }
        }
    }
}

private fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.cacheDir // Use the cache directory
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}
