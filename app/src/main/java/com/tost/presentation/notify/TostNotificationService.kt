package com.tost.presentation.notify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tost.R
import com.tost.presentation.SplashActivity
import com.tost.presentation.utils.printLog

/**
 * Created By Malibin
 * on 11월 21, 2020
 */

class TostNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notificationManager = getNotificationManager()
        val notification = createNotification(remoteMessage.data, getDeployingAppIntent())
        notificationManager.notify(0, notification)
        printLog(remoteMessage.data.toString())
    }

    private fun getDeployingAppIntent(): PendingIntent {
        val intent = Intent(this, SplashActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    private fun getNotificationManager(): NotificationManager {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createNotificationChannel())
        }
        return notificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            this.enableLights(true)
            this.lightColor = 0xFFFF4300.toInt()
        }
    }

    private fun createNotification(
        message: Map<String, String>,
        pendingIntent: PendingIntent
    ): Notification {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_final)
            .setContentTitle(message[TITLE_KEY])
            .setContentText(message[CONTENT_KEY])
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(createBigTextStyle(message))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notification.priority = NotificationManager.IMPORTANCE_HIGH
        }
        return notification.build()
    }

    private fun createBigTextStyle(message: Map<String, String>): NotificationCompat.BigTextStyle {
        return NotificationCompat.BigTextStyle()
            .setBigContentTitle(message[TITLE_KEY])
            .bigText(message[CONTENT_KEY])
    }

    override fun onNewToken(token: String) {
        printLog("on new Fcm Token = $token")
    }

    companion object {
        private const val CHANNEL_NAME = "TostChannel"
        private const val CHANNEL_ID = "TostId"
        private const val TITLE_KEY = "title"
        private const val CONTENT_KEY = "content"
    }
}