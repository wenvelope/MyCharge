package com.example.mycharge.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChargeService {
    @GET("charges")
    fun searchCharges():Call<ChargePlace>
}

interface FormService {
    @GET("charges/forms")
    fun getForms(@Query("token") token:String):Call<Forms>
}

interface PostFormService{
    @GET("charges/postForms")
    fun postForms(@Query("token") token: String,@Query("start") start:String, @Query("consume") consume:String ):Call<ResponseBody>
}