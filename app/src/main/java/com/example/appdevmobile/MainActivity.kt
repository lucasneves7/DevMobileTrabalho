package com.example.appdevmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.appdevmobile.core.Config
import com.example.appdevmobile.view.HomeActivity
import com.example.appdevmobile.view.login.LoginActivity
import com.example.appdevmobile.view.login.LoginViewModel
import com.example.appdevmobile.view.register.RegisterVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

/**
 * MainActivity é a atividade principal que gerencia a inicialização do aplicativo.
 * Ela verifica se o usuário já está autenticado e redireciona para a tela apropriada.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o Koin para injeção de dependências, se ainda não estiver inicializado.
        if (savedInstanceState == null) {
            startKoin {
                // Define o contexto do Android para o Koin
                androidContext(this@MainActivity)
                // Registra os módulos de dependências do Koin
                modules(appModule)
            }
        }

        // Verifica se o usuário já está autenticado
        if (Config.hasLogin(this)) {
            // Se estiver autenticado, redireciona para a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            // Se não estiver autenticado, redireciona para a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Finaliza a MainActivity para evitar que o usuário volte para esta tela
        finish()
    }
}

/**
 * Módulo de injeção de dependências do Koin.
 * Define quais ViewModels serão disponibilizados para injeção.
 */
private val appModule = module {
    viewModel { RegisterVM() } // ViewModel responsável pelo cadastro de usuários
    viewModel { LoginViewModel() } // ViewModel responsável pelo login de usuários
}