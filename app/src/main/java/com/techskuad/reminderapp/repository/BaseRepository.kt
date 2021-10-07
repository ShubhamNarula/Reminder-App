package com.techskuad.reminderapp.repository

import com.techskuad.reminderapp.network.ApiResponseWrapper
import com.techskuad.reminderapp.utilities.AppLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

const val GENERIC_ERROR_CODE = 1001
const val UNAUTHORIZED_ERROR_CODE = 401
abstract  class BaseRepository {
    protected suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ApiResponseWrapper<T> {
        return withContext(dispatcher) {
            try {
                ApiResponseWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ApiResponseWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        if (code == UNAUTHORIZED_ERROR_CODE) {
                            ApiResponseWrapper.UnauthorizedError
                        } else {
                            val errorResponse = getErrorMessage(throwable.response()?.errorBody())
                            ApiResponseWrapper.GenericError(code, errorResponse)
                        }
                    }
                    else -> {
                        //handle according to use case
                        AppLog.e(throwable.message ?: "")
                        ApiResponseWrapper.GenericError(GENERIC_ERROR_CODE, throwable.message)
                    }
                }
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            val jsonObject = JSONObject(responseBody.toString())
            val errorObject = jsonObject.getJSONObject("error")
            errorObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }
}