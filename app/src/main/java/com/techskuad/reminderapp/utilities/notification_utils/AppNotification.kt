package com.app.easyeat.utils.notification_utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.techskuad.reminderapp.MainActivity
import com.techskuad.reminderapp.R


class AppNotification private constructor(
    private val context: Context,
    private val channelName: String,
    private val channelId: String, private val builder: Builder
) {

    private val DEFAULT_SOUND =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    private val DEFAULT_VIBRATION_ARRAY = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

    private fun buildNotification(): NotificationCompat.Builder {
        createNotificationChannel(
            channelName,
            channelId
        )
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(builder.smallIcon)
            .setContentTitle(builder.title)
            .setContentText(builder.description)
            .setAutoCancel(builder.autoCancel)
            .setPriority(builder.priority)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSound(DEFAULT_SOUND)
            .setVibrate(DEFAULT_VIBRATION_ARRAY)
            .setContentIntent(PendingIntent.getActivity(context, 0, builder.intent, 0))
    }

    fun createNotification(notificationId: Int) {
        val notificationBuilder = buildNotification()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notificationBuilder.build())
        }
    }



    class Builder constructor(
        private val context: Context,
        private val channelName: String,
        private val channelId: String
    ) {
        var title: String? = null
        var description: String? = null
        var bigDescription: String? = null
        var smallIcon: Int = R.drawable.ic_launcher_foreground
        var priority: Int = NotificationCompat.PRIORITY_HIGH
        var autoCancel: Boolean = true
        var intent: Intent = Intent(context, MainActivity::class.java)

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setBigDescription(bigDescription: String): Builder {
            this.bigDescription = bigDescription
            return this
        }

        fun setSmallIcon(smallIcon: Int): Builder {
            this.smallIcon = smallIcon
            return this
        }

        fun setPriority(priority: Int): Builder {
            this.priority = priority
            return this
        }

        fun setAutoCancel(autoCancel: Boolean): Builder {
            this.autoCancel = autoCancel
            return this
        }

        fun setIntent(intent: Intent): Builder {
            this.intent = intent
            return this
        }

        fun build(): AppNotification {
            return AppNotification(context, channelName, channelId, this)
        }
    }


    private fun createNotificationChannel(channelName: String, channelId: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
                enableLights(true)
                setSound(DEFAULT_SOUND, attributes)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = DEFAULT_VIBRATION_ARRAY
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}