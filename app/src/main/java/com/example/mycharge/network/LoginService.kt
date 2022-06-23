package com.example.mycharge.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("users/login")
    fun getToken(@Query("username") username:String,@Query("password") password:String):Call<ResponseBody>
}

interface SignService{
    @GET("users/sign")
    fun Sign(@Query("username") username:String,@Query("password") password:String):Call<ResponseBody>
}