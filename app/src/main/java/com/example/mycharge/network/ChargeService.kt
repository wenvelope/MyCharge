package com.example.mycharge.network

import retrofit2.Call
import retrofit2.http.GET

interface ChargeService {
    @GET("charges")
    fun searchCharges():Call<ChargePlace>
}