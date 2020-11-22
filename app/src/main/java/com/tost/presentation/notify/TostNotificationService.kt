package com.tost.presentation.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tost.R
import com.tost.presentation.MainActivity
import com.tost.presentation.utils.printLog

/**
 * Created By Malibin
 * on 11월 21, 2020
 */

class TostNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "Tost Channel"
            val channelId = "TostId"
            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = 0xFFFF4300.toInt()

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("ContentTitle")
                .setContentText("Content Text 입니당")
                .setVibrate(longArrayOf(500L, 500L, 500L, 500L))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLights(0xFFFF4300.toInt(), 1500, 1000)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(0, notification)
        }
        val data = remoteMessage.data
        printLog("title : ${data["title"]}, content : ${data["content"]}")

    }

    override fun onNewToken(token: String) {
        printLog("on new Fcm Token = $token")
    }
}