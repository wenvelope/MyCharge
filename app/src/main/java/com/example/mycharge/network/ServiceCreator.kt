package com.example.mycharge.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "http://110.40.156.9:4000/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun<T> create(serviceClass:Class<T>):T = retrofit.create(serviceClass)

    inline fun<reified T> create():T = create(T::class.java)
}