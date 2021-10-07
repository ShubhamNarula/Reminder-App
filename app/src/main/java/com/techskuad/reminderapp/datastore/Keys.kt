package com.template.datastore

import androidx.datastore.preferences.core.stringPreferencesKey


const val DATA_STORE_NAME = "Reminder_app_db"
//key name
val MODE by lazy { stringPreferencesKey("MODE") }



