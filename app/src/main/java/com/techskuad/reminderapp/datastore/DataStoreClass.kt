package com.techskaud.sampleapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.gson.Gson
import com.techskuad.reminderapp.utilities.dataStore
import com.template.datastore.MODE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreClass @Inject
constructor(@ApplicationContext private val context: Context) {
      suspend fun saveToLocal(data: String) {
        context.dataStore.edit { preference->
            preference[MODE] = data
        }
    }

    val readFromLocal : Flow<String> = context.dataStore.data
        .catch {
            if(this is Exception){
                emit(emptyPreferences())
            }
        }.map { preference->
            val data = preference[MODE] ?:""
            data
        }

}


