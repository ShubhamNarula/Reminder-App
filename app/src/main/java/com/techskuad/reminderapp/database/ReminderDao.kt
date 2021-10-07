package com.techskuad.reminderapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techskuad.reminderapp.model.ReminderModel
import androidx.room.FtsOptions.Order

import androidx.room.Update




@Dao
interface ReminderDao {

    @Insert
    suspend fun addReminder(data: ReminderModel)

    @Insert
    fun insert(data: ReminderModel)

    @Query("SELECT * FROM reminder_table ORDER BY id DESC")
    fun getAllReminderList(): LiveData<List<ReminderModel>>

    @Query("SELECT * FROM reminder_table where id=:id")
    fun getReminderById(id:Int):ReminderModel

    @Query("Update reminder_table set title =:msg,description=:desc,time=:time,date=:date,type=:type where id=:id")
    fun updateReminder(msg:String,desc:String,time:String,date:String,type:String,id: Int)

    @Update(entity = ReminderModel::class)
    fun update(reminderData: ReminderModel?)

}