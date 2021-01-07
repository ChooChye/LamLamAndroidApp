package com.choochyemeilin.lamlam.helpers


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.choochyemeilin.lamlam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


object Utils {

    lateinit var fadeInTop: Animation
    lateinit var fadeInBottom: Animation
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val fbAuth : FirebaseAuth = FirebaseAuth.getInstance()

    fun declareAnim(context: Context) {
        fadeInTop = AnimationUtils.loadAnimation(context, R.anim.fade_in_top)
        fadeInBottom = AnimationUtils.loadAnimation(context, R.anim.fade_in_bottom)
    }

    //get current date
    fun now() : String{
        val c : Calendar = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val now = "$year-$month-$day $hour:$minute"

        return now
    }

    fun log(msg: String){
        Log.e("TEST", msg)
    }

    fun toast(context: Context, msg: String, duration: Int){ // duration : 0 = short || >= 1 Long
        if(duration === 0){
            return Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }else{
            return Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    //Showing the keyboard
    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    //Hiding the keyboard
   fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getStaffID(callback: FbCallback) : Int{
        val user = fbAuth.currentUser?.email
        var staffID  = 0
        val myRef: DatabaseReference = database.getReference("User")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val staffEmail = dss.child("staffEmail").value.toString()
                    if (user == staffEmail) {
                        val staffID = dss.child("staffID").value.toString().toInt()
                        callback.onCallbackGetUserID(staffID!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                log("Error has occurred #9372 | ${error.message}")
            }
        })
        return staffID
    }

    fun getRetailerID(callback: FbCallback) : Int{
        val user = fbAuth.currentUser?.email
        var retailerID  = 0
        val myRef: DatabaseReference = database.getReference("User")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val staffEmail = dss.child("staffEmail").value.toString()
                    if (user == staffEmail) {
                        val retailerID = dss.child("retailerID").value.toString().toInt()

                        callback.onCallbackGetUserID(retailerID)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.log("Error has occurred #9372 | ${error.message}")
            }
        })
        return retailerID
    }

    fun getRetailerInfo(callback: FbCallback){
        val user = fbAuth.currentUser?.email
        var retailerID : Int? = 0

        getRetailerID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                super.onCallbackGetUserID(uid)
                retailerID = uid
            }
        })

        val myRef: DatabaseReference = database.getReference("Retailers")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val dbRetailerID = dss.child("retailerID").value.toString().toInt()
                    if (retailerID == dbRetailerID) {
                        val retailerName = dss.child("retailerName").value.toString()
                        val retailerAddress = dss.child("retailerAddress").value.toString()
                        val arr: ArrayList<Retailers> = ArrayList()
                        arr.add(Retailers(dbRetailerID, retailerName, retailerAddress))
                        callback.onCallbackRetailer(arr)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.log("Error has occurred #9373 | ${error.message}")
            }
        })
    }

    fun checkUserAuth() : Boolean{
        val user = fbAuth.currentUser
        var status = false
        if(user != null){
            status = true
        }
        return status
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}