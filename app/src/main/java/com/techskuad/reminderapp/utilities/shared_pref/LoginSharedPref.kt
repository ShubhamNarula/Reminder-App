package com.app.easyeat.utils.shared_pref

import android.content.Context
import com.techskuad.reminderapp.base.BaseSharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSharedPref @Inject constructor(@ApplicationContext context: Context) :
    BaseSharedPref(
        context.getSharedPreferences(
            SharedPrefName.LOGIN_SHARED_PREF.name,
            Context.MODE_PRIVATE
        )
    ) {

    val isUserLoggedIn: String = SharedPrefName.LOGIN_SHARED_PREF.name + "_isUserLoggedIn"
}