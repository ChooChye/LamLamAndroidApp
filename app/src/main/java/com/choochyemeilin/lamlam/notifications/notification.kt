package com.choochyemeilin.lamlam.notifications

import android.R
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.ReturnItems.MyStocks
import com.choochyemeilin.lamlam.ReturnItems.MyStocksAdapter
import com.choochyemeilin.lamlam.ReturnItems.ReturnItems
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.choochyemeilin.lamlam.notifications.NotificationApp.Companion.CHANNEL_1_ID
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_stocks.*
import kotlinx.android.synthetic.main.activity_notification.*
import java.util.*

class notification : AppCompatActivity(), View.OnClickListener {

    private var notificationManager: NotificationManagerCompat? = null
    private var staffID: Int? = 0
    private val notificationId=1
    private var message = "MESSAGE TESTING"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.choochyemeilin.lamlam.R.layout.activity_notification)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "Update Reminder Time"

        notificationManager = NotificationManagerCompat.from(this)

        button_notification_set.setOnClickListener(this)
        button_notification_cancel.setOnClickListener(this)

        showMessage(object : FbCallback {
            override fun onCallbackGetNotificationInfo(msg: String) {
                super.onCallbackGetNotificationInfo(msg)
                textView_message.text=msg
            }
        })

     }


     @RequiresApi(Build.VERSION_CODES.M)
     override fun onClick(p0: View?) {

         // Intent
         val intent = Intent(this, NotificationReceiver::class.java)

         intent.putExtra("notificationId", notificationId)
         intent.putExtra("message", message)


         // PendingIntent
         val pendingIntent = PendingIntent.getBroadcast(
             this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
         )

         // AlarmManager
         val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

         if (p0 != null) {
             button_notification_set.setOnClickListener {
                 val hour = timePicker.currentHour
                 val minute = timePicker.currentMinute

                 // Create time.
                 val startTime = Calendar.getInstance()
                 startTime[Calendar.HOUR_OF_DAY] = hour
                 startTime[Calendar.MINUTE] = minute
                 startTime[Calendar.SECOND] = 0
                 val alarmStartTime = startTime.timeInMillis

                 // Set Alarm
                 alarmManager[AlarmManager.RTC_WAKEUP, alarmStartTime] = pendingIntent
                 Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
             }

             button_notification_cancel.setOnClickListener {
                 alarmManager.cancel(pendingIntent)
                 val intent = Intent(this, MyStocks::class.java)
                 startActivity(intent)

             }

         }
     }

    fun showMessage(callback: FbCallback){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (dss in snapshot.children) {
                        dss.children.forEachIndexed { index, it ->
                            val dbSID = it.child("staffID").value.toString().toInt()
                            val status = it.child("status").value.toString()
                            val loanID = it.child("loanID").value.toString()

                            Utils.getStaffID(object : FbCallback {
                                override fun onCallbackGetUserID(uid: Int) {
                                    super.onCallbackGetUserID(uid)
                                    staffID = uid

                                    if (staffID == dbSID) {
                                        if (status.toUpperCase() == "APPROVED") {

                                            val product = it.child("productName")
                                            val product1 = it.child("productName").value.toString()

                                            message="Loan ID :"+loanID +"\n"+"Product :"+product1+"\n"
                                            callback.onCallbackGetNotificationInfo(message)

                                            /*product.children.forEach {

                                                val key = it.key.toString()
                                                val qty = it.value.toString()

                                                    message="Loan ID :"+loanID +"\n"+"Product :"+product1+"\n"

                                                callback.onCallbackGetNotificationInfo(message)
                                            }*/

                                        }
                                    }
                                }
                            })

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Utils.toast(applicationContext, error.message, 1)
                }

            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

 }

