package com.techskuad.reminderapp.repository

import com.techskuad.reminderapp.model.LoginRequestModel
import com.techskuad.reminderapp.model.LoginResponse
import com.techskuad.reminderapp.network.ApiResponseWrapper
import com.techskuad.reminderapp.network.LoginApi
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val loginApi: LoginApi) : BaseRepository()  {
    suspend fun callLoginApi(loginReq:LoginRequestModel): ApiResponseWrapper<LoginResponse> {
        return safeApiCall(Dispatchers.IO) {
            loginApi.loginApi(loginReq)
        }
    }

}