package com.techskuad.reminderapp.utilities.notification_utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.techskuad.reminderapp.utilities.Constants
import com.techskuad.reminderapp.worker.ReminderNotificationWorker
import java.util.*
import java.util.concurrent.TimeUnit
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequest
import com.google.type.DateTime
import com.techskuad.reminderapp.utilities.TimeUtils
import java.text.SimpleDateFormat

import java.time.LocalDateTime.now


class NotificationUtils {

    fun addReminder(
        context: Context,
        reminderType: String,
        timeInMilliSeconds: Long,
        reminderDesc: String,
        reminderTitle: String,
        reminderTime: String,
        reminderId: Int
    ) {
        val data = Data.Builder()
        data.putString(Constants.REMINDER_TYPE, reminderType)
        data.putString(Constants.REMINDER_TITTLE, reminderTitle)
        data.putString(Constants.REMINDER_DESC, reminderDesc)
        data.putString(Constants.REMINDER_TIME, reminderTime)
        data.putInt(Constants.REMINDER_ID, reminderId)
        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val exactTime = timeInMilliSeconds - currentTimeInMills
        val work =
            OneTimeWorkRequest.Builder(ReminderNotificationWorker::class.java)
                .setInitialDelay(exactTime, TimeUnit.MILLISECONDS)
                .setInputData(data.build())
                .addTag(System.currentTimeMillis().toString())
                .build()
        WorkManager.getInstance(context).enqueue(work)
    }


    private val REMINDER_WORK_NAME = "reminder"
    private val PARAM_NAME = "name" // optional - send parameter to worker
    // private const val RESULT_ID = "id"

    @RequiresApi(Build.VERSION_CODES.O)
    fun runAt(
        context: Context, reminderType: String,
        timeInMilliSeconds: Long,
        reminderDesc: String,
        reminderTitle: String,
        reminderTime: String,
        reminderId: Int
    ) {
        val workManager = WorkManager.getInstance(context)

        // trigger at 8:30am
        val alarmTime = LocalTime.of(10, 30)
        var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        val nowTime = now.toLocalTime()
        // if same time, schedule for next day as well
        // if today's time had passed, schedule for next day
        if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) {
            now = now.plusDays(1)
        }
        now = now.withHour(alarmTime.hour)
            .withMinute(alarmTime.minute) // .withSecond(alarmTime.second).withNano(alarmTime.nano)
        val duration = java.time.Duration.between(LocalDateTime.now(), now)

        // optional data
        val data = Data.Builder()
        data.putString(Constants.REMINDER_TYPE, reminderType)
        data.putString(Constants.REMINDER_TITTLE, reminderTitle)
        data.putString(Constants.REMINDER_DESC, reminderDesc)
        data.putString(Constants.REMINDER_TIME, reminderTime)
        data.putInt(Constants.REMINDER_ID, reminderId)
        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val exactTime = timeInMilliSeconds - currentTimeInMills

        val workRequest = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
            .setInitialDelay(duration.seconds, TimeUnit.SECONDS)
            // .setConstraints(constraints)
            .setInputData(data.build()) // optional
            .build()

        workManager.enqueueUniqueWork(REMINDER_WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun cancel() {
        val workManager = WorkManager.getInstance()
        workManager.cancelUniqueWork(REMINDER_WORK_NAME)
    }


    fun addFutureReminder(
        context: Context,
        reminderType: String,
        timeInMilliSeconds: Long,
        reminderDesc: String,
        reminderTitle: String,
        reminderTime: String,
        reminderId: Int
    ) {
        val data = Data.Builder()
        data.putString(Constants.REMINDER_TYPE, reminderType)
        data.putString(Constants.REMINDER_TITTLE, reminderTitle)
        data.putString(Constants.REMINDER_DESC, reminderDesc)
        data.putString(Constants.REMINDER_TIME, reminderTime)
        data.putInt(Constants.REMINDER_ID, reminderId)
        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val exactTime = timeInMilliSeconds - currentTimeInMills

        val sdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT)
        val selectedMonthAndDate = TimeUtils.getFutureDate(Constants.DATE_FORMAT, 1)
        val selectedUserTime = selectedMonthAndDate + " " + reminderTime
        val date = sdf.parse(selectedUserTime)
        val millis = date.getTime()

//        val workRequest = PeriodicWorkRequest.Builder(
//            ReminderNotificationWorker::class.java,
//            millis,
//            TimeUnit.MILLISECONDS,
//            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
//            TimeUnit.MILLISECONDS
//        )
//            .setInitialDelay(exactTime, TimeUnit.MILLISECONDS)
//            .addTag("send_reminder_periodic")
//            .setInputData(data.build())
//            .build()
//
//
//        WorkManager.getInstance(context)
//            .enqueueUniquePeriodicWork(
//                "send_reminder_periodic",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                workRequest
//            )
        val workRequest = PeriodicWorkRequest.Builder(
            ReminderNotificationWorker::class.java,
            24,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.HOURS
        )
            .setInitialDelay(exactTime, TimeUnit.MILLISECONDS)
            .addTag("send_reminder_periodic")
            .setInputData(data.build())
            .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "send_reminder_periodic",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }
}