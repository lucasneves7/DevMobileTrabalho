package com.example.appdevmobile.view.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appdevmobile.MainActivity
import com.example.appdevmobile.core.Config
import com.example.appdevmobile.ui.theme.AppDevMobileTheme
import com.example.appdevmobile.view.UiState
import com.example.appdevmobile.view.register.RegisterActivity
import org.koin.androidx.compose.koinViewModel

/**
 * Tela de login da aplicação.
 *
 * Essa Activity exibe um formulário de login e gerencia a navegação para a tela principal (`MainActivity`)
 * ou para a tela de cadastro (`RegisterActivity`).
 */
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ativa o suporte a tela cheia.

        setContent {
            AppDevMobileTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        item {
                            LoginScreen {
                                // Após login bem-sucedido, navega para a MainActivity e finaliza a LoginActivity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tela de login representada por um composable.
 *
 * @param viewModel O ViewModel responsável pelo login.
 * @param onFinish Callback chamado após um login bem-sucedido.
 */
@Composable
private fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val uiState by remember { derivedStateOf { viewModel.uiState } }

    // Estados para armazenar usuário e senha
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }

    // Observa mudanças no estado da UI e executa ações apropriadas
    LaunchedEffect(uiState) {
        if (uiState !is UiState.Success) return@LaunchedEffect
        // Salva as credenciais do usuário após login bem-sucedido
        Config.setLogin(context, usuario)
        Config.setPassword(context, senha)
        onFinish() // Chama o callback para finalizar a tela de login
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Social Ifes",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada para o usuário
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Usuário") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada para a senha com ocultação de caracteres
        TextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botão para ir à tela de cadastro
            TextButton(
                onClick = {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                },
                enabled = uiState !is UiState.Loading // Desabilita botão se estiver carregando
            ) {
                Text("Cadastrar")
            }

            // Botão de login
            Button(
                onClick = { viewModel.login(usuario, senha) },
                enabled = uiState !is UiState.Loading // Desabilita botão se estiver carregando
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Entrar")
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
                Text("Login realizado com sucesso!", color = Color.Green)
            }
            else -> {}
        }
    }
}
