package com.example.appdevmobile

import android.app.Application
import com.example.appdevmobile.view.login.LoginViewModel
import com.example.appdevmobile.view.register.RegisterVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

// Classe de aplicativo principal da aplicação.
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializa o Koin para injeção de dependências.
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
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