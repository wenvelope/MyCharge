package com.example.mycharge.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ChargeNetwork {
    private val loginService = ServiceCreator.create(LoginService::class.java)
    private val SignService = ServiceCreator.create(SignService::class.java)
    private val ChargeService = ServiceCreator.create(ChargeService::class.java)
    suspend fun getToken(username:String,password:String) = loginService.getToken(username,password).await()
    suspend fun Sign(username: String,password: String) = SignService.Sign(username,password).await()
    suspend fun serachCharges() = ChargeService.searchCharges().await()

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine{
            continuation -> enqueue(object :Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if(body!=null) continuation.resume(body)
                else continuation.resumeWithException(
                    RuntimeException("response body is null")
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
        }
    }
}