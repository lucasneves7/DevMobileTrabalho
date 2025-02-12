package com.example.appdevmobile.repository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://socialifesweb-bojnmat9.b4a.run/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}