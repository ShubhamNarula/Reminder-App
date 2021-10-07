package com.techskuad.reminderapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techskuad.reminderapp.model.ReminderModel

@Database(entities = [ReminderModel::class], version = 1, exportSchema = false)
abstract class ReminderDatabase : RoomDatabase(){
    abstract fun reminderDao(): ReminderDao

    companion object {
        private var INSTANCE: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            if (INSTANCE == null) {
                synchronized(ReminderDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Get PhraseRoomDatabase database instance
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ReminderDatabase::class.java, "reminder_db"
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}