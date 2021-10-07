package com.techskuad.reminderapp.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import com.template.datastore.DATA_STORE_NAME

/*Use to set recycler adapter view*/
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)
