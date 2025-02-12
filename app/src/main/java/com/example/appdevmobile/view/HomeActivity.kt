package com.example.appdevmobile.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.appdevmobile.MainActivity
import com.example.appdevmobile.core.Config
import com.example.appdevmobile.ui.theme.AppDevMobileTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppDevMobileTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Text("Logado com sucesso!")
                        Button(
                            onClick = {
                                Config.logout(this@HomeActivity)
                                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            Text("Logout")
                        }
                    }
                }
            }
        }
    }
}