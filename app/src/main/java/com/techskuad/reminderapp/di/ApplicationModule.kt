package com.app.easyeat.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.techskuad.reminderapp.ReminderApplication
import com.techskuad.reminderapp.database.ReminderDao
import com.techskuad.reminderapp.database.ReminderDatabase
import com.techskuad.reminderapp.repository.ReminderDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {
    @Provides
    @Singleton
    internal fun provideApplicationClass(application: Application): ReminderApplication {
        return application as ReminderApplication
    }

    @Singleton
    @Provides
    fun provideData(@ApplicationContext context: Context) : ReminderDatabase =
        Room.databaseBuilder(context,ReminderDatabase::class.java,"reminder_db").allowMainThreadQueries()
            .build()
    @Provides
    fun providesDao(applicationDatabase: ReminderDatabase):ReminderDao=
        applicationDatabase.reminderDao()

    @Provides
    fun signupRepo(signUpDao: ReminderDao):ReminderDbRepository =
        ReminderDbRepository(signUpDao)
}