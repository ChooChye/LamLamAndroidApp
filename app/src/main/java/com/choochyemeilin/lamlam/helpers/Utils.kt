package com.choochyemeilin.lamlam.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*



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

    fun getUser(){
        val user = fbAuth.currentUser

        val myRef: DatabaseReference = database.getReference("User")

    }

    fun checkUserAuth() : Boolean{
        val user = fbAuth.currentUser
        var status = false
        if(user != null){
            status = true
        }
        return status
    }

    fun forceLogin(context : Context){
        val intent = Intent(context, Login::class.java)
        startActivity(context, intent,  null)
    }
}