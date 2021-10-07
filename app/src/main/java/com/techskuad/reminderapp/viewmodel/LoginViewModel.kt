package com.techskuad.reminderapp.viewmodel

import android.util.Patterns
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.easyeat.utils.shared_pref.LoginSharedPref
import com.techskaud.sampleapp.datastore.DataStoreClass
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.ReminderApplication
import com.techskuad.reminderapp.model.LoginRequestModel
import com.techskuad.reminderapp.network.ApiResponseWrapper
import com.techskuad.reminderapp.repository.LoginRepository
import com.techskuad.reminderapp.utilities.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(
    val context: ReminderApplication,
    private val loginRepository: LoginRepository,
    val loginSharedPref: LoginSharedPref
) : BaseViewModel(context) {


    val email = MutableLiveData<String>("eve.holt@reqres.in")
    val password = MutableLiveData<String>("cityslicka")

    val emailError: MutableLiveData<String?> = MutableLiveData(null)
    val passwordError: MutableLiveData<String?> = MutableLiveData(null)

    fun login(view: View) {
        if (isValidCredentials()) {
            val loginRequest = LoginRequestModel(
                email = email.value.toString(),
                password = password.value.toString()
            )
            isLoading.postValue(true)

            viewModelScope.launch {
                when (val response = loginRepository.callLoginApi(loginRequest)) {
                    is ApiResponseWrapper.Success -> {
                        isLoading.postValue(false)
                        if (response.value.token !="") {
                            //move to enter otp fragment
                            loginSharedPref.save(loginSharedPref.isUserLoggedIn, "Yes")
                            view.navigateWithId(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            displayError.postValue(response.value.error)
                        }
                    }
                    is ApiResponseWrapper.NetworkError -> {
                        handleNetworkError()
                    }
                    is ApiResponseWrapper.UnauthorizedError -> {
                        handleUnauthorizedError()
                    }
                    is ApiResponseWrapper.GenericError -> {
                        isLoading.postValue(false)
                        displayError.postValue(response.errorMessage)
                    }
                    else -> {
                    }
                }
            }

        }


    }
    private fun isValidCredentials(): Boolean {
        var isValid = true
        if (password.value.isNullOrBlank()) {
            isValid = false
            setEmptyError(passwordError)
        }else if (password.value !="cityslicka"){
            isValid = false
            passwordError.postValue(context.getString(R.string.invalid_pass))
        }
        if (email.value.isNullOrBlank()) {
            isValid = false
            setEmptyError(emailError)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            isValid = false
            emailError.postValue(context.getString(R.string.invalid_email))
        }
        return isValid
    }

    fun setEmptyError(field: MutableLiveData<String?>) {
        field.postValue(context.getString(R.string.required_field))
    }




}