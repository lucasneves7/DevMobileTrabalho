package com.example.appdevmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.appdevmobile.core.Config
import com.example.appdevmobile.view.HomeActivity
import com.example.appdevmobile.view.login.LoginActivity

/**
 * MainActivity é a atividade principal que gerencia a inicialização do aplicativo.
 * Ela verifica se o usuário já está autenticado e redireciona para a tela apropriada.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se o usuário já está autenticado
        if (Config.hasLogin(this@MainActivity)) {
            // Se estiver autenticado, redireciona para a HomeActivity
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        } else {
            // Se não estiver autenticado, redireciona para a LoginActivity
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // Finaliza a MainActivity para evitar que o usuário volte para esta tela
        finish()
    }
}