package com.techskuad.reminderapp.network

import com.techskuad.reminderapp.model.LoginRequestModel
import com.techskuad.reminderapp.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("login?")
    suspend fun loginApi(@Body loginReq : LoginRequestModel): LoginResponse
}