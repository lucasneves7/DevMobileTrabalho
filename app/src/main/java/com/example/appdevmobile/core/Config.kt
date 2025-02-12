package com.example.appdevmobile.core

import android.content.Context

/**
 * Objeto responsável por gerenciar as configurações de autenticação do usuário.
 * Utiliza SharedPreferences para armazenar credenciais de login e senha de forma local.
 */
object Config {

    /**
     * Salva o login do usuário no SharedPreferences.
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     * @param login O login do usuário a ser armazenado.
     */
    fun setLogin(context: Context, login: String) {
        val sharedPreferences = context.getSharedPreferences("configs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("login", login)
        editor.apply() // Aplica a modificação de forma assíncrona.
    }

    /**
     * Obtém o login armazenado do usuário.
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     * @return O login do usuário ou `null` se não estiver definido.
     */
    fun getLogin(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("configs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("login", null)
    }

    /**
     * Salva a senha do usuário no SharedPreferences.
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     * @param password A senha do usuário a ser armazenada.
     */
    fun setPassword(context: Context, password: String) {
        val sharedPreferences = context.getSharedPreferences("configs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("senha", password)
        editor.apply() // Aplica a modificação de forma assíncrona.
    }

    /**
     * Obtém a senha armazenada do usuário.
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     * @return A senha do usuário ou `null` se não estiver definida.
     */
    fun getPassword(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("configs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("senha", null)
    }

    /**
     * Verifica se há credenciais salvas no dispositivo.
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     * @return `true` se tanto login quanto senha estiverem armazenados, `false` caso contrário.
     */
    fun hasLogin(context: Context): Boolean {
        return !getLogin(context).isNullOrBlank() && !getPassword(context).isNullOrBlank()
    }

    /**
     * Gera um cabeçalho de autenticação Basic Auth a partir de login e senha.
     *
     * @param login O login do usuário.
     * @param password A senha do usuário.
     * @return Uma string codificada em Base64 no formato "Basic <credenciais>".
     */
    fun auth(login: String, password: String): String {
        return "Basic " + android.util.Base64.encodeToString(
            "$login:$password".toByteArray(), android.util.Base64.NO_WRAP
        )
    }

    /**
     * Remove as credenciais armazenadas (logout do usuário).
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     */
    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("configs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("login") // Remove o login armazenado.
        editor.remove("senha") // Remove a senha armazenada.
        editor.apply() // Aplica a modificação de forma assíncrona.
    }

    /**
     * Gera um cabeçalho de autenticação Basic Auth a partir das credenciais armazenadas.
     *
     * @param context Contexto da aplicação necessário para acessar SharedPreferences.
     * @return Uma string codificada em Base64 no formato "Basic <credenciais>", ou `null` se não houver credenciais armazenadas.
     */
    fun auth(context: Context): String? {
        val _login = getLogin(context) ?: return null
        val _password = getPassword(context) ?: return null
        return "Basic " + android.util.Base64.encodeToString(
            "$_login:$_password".toByteArray(), android.util.Base64.NO_WRAP
        )
    }
}