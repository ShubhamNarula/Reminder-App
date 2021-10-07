package com.techskuad.reminderapp.repository

import androidx.lifecycle.LiveData
import com.techskuad.reminderapp.database.ReminderDao
import com.techskuad.reminderapp.model.ReminderModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReminderDbRepository @Inject constructor(val reminderDao: ReminderDao) {
    val getAllReminderList: LiveData<List<ReminderModel>> = reminderDao.getAllReminderList()

    fun getReminderById(id: Int) {
        reminderDao.getReminderById(id)
    }
    suspend fun insert(data: ReminderModel) = withContext(Dispatchers.IO){
        reminderDao.addReminder(data)
    }
}