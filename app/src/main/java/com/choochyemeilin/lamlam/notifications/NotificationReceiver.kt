package com.choochyemeilin.lamlam.notifications

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.choochyemeilin.lamlam.MainActivity


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat.startActivity
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.ReturnItems.ReturnItems

class NotificationReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "CHANNEL_SAMPLE"

    override fun onReceive(context: Context, intent: Intent) {
        //  val message = intent.getStringExtra("toastMessage")
        //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        /*      val service = Intent(context, NotificationService::class.java)
              service.putExtra("reason", intent.getStringExtra("reason"))
              service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))

              context?.startService(service)*/

        // Get id & message
        val notificationId = intent.getIntExtra("notificationId", 0)
        val message = intent.getStringExtra("message")

        // Call MainActivity when notification is tapped.
        val mainIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0)

        // NotificationManager
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            val channelName: CharSequence = "My Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }


      /*  val broadcastIntent = Intent(context, ReturnItems::class.java)
      //  broadcastIntent.putExtra("toastMessage", message)
        val actionIntent = PendingIntent.getBroadcast(
            context,
            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
*/
        val notifyIntent = Intent(context, ReturnItems::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        // Prepare Notification
        val builder: Notification? = NotificationCompat.Builder(context, CHANNEL_ID)

            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle("DUE DATE CLOSE")
            .setContentText(message)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setOnlyAlertOnce(true)
            .addAction(com.choochyemeilin.lamlam.R.mipmap.ic_launcher, "Return Now", notifyPendingIntent)

            .build()

        // Notify
      //  notificationManager.notify(notificationId, builder.build())
        notificationManager!!.notify(1, builder)
    }
}