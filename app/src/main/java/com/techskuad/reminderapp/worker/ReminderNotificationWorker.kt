package com.techskuad.reminderapp.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.app.easyeat.utils.notification_utils.AppNotification
import com.app.easyeat.utils.notification_utils.AppNotificationChannel
import com.techskuad.reminderapp.database.ReminderDatabase
import com.techskuad.reminderapp.model.ReminderModel
import com.techskuad.reminderapp.utilities.Constants
import com.techskuad.reminderapp.utilities.TimeUtils
import com.techskuad.reminderapp.utilities.notification_utils.NotificationUtils
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class ReminderNotificationWorker  constructor( appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private lateinit var reminderType: String
    private lateinit var reminderDesc: String
    private lateinit var reminderTitle: String
    private lateinit var reminderTime: String
    private var reminderId: Int = 0

    private lateinit var reminderDatabase: ReminderDatabase


    override fun doWork(): Result {
        reminderType = inputData.getString(Constants.REMINDER_TYPE).toString()
        reminderTitle = inputData.getString(Constants.REMINDER_TITTLE).toString()
        reminderDesc = inputData.getString(Constants.REMINDER_DESC).toString()
        reminderTime = inputData.getString(Constants.REMINDER_TIME).toString()
        reminderId = inputData.getInt(Constants.REMINDER_ID, 0)
        reminderDatabase= ReminderDatabase.getDatabase(applicationContext)


        AppNotification.Builder(
            context = applicationContext,
            AppNotificationChannel.ReminderNotification.name,
            AppNotificationChannel.ReminderNotification.id
        )
            .setTitle(reminderTitle ?: "")
            .setDescription(reminderDesc ?: "")
            .build().createNotification(System.currentTimeMillis().toInt())
        setReminder()
        return Result.success()
    }

    private fun setReminder() {
        var selectedUserTime = ""
        val sdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT)
        var days=7
        if (reminderType == "daily"){
            days=1
        }
        val selectedMonthAndDate = TimeUtils.getFutureDate(Constants.DATE_FORMAT,days)
        selectedUserTime = selectedMonthAndDate + " " + reminderTime
        val date = sdf.parse(selectedUserTime)
        val millis = date.getTime()

        val reminderId = System.currentTimeMillis().toInt()
        val reminderData = ReminderModel(reminderTitle,reminderDesc,
            reminderTime, selectedMonthAndDate,reminderType)
        reminderDatabase.reminderDao().insert(reminderData)

//        NotificationUtils().addReminder(
//            applicationContext, reminderType, millis,
//            reminderDesc, reminderTitle, reminderTime, reminderId
//        )


    }
}