package com.choochyemeilin.lamlam.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


class NotificationApp : android.app.Application() {
    val CHANNEL_1_ID = "channel1"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is Channel 1"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel1)

        }
    }

    companion object {
        const val CHANNEL_1_ID = "channel1"

    }
}