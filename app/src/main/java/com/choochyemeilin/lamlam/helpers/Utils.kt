package com.choochyemeilin.lamlam.helpers

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Register.Register
import java.time.Duration


object Utils {

    lateinit var fadeInTop: Animation
    lateinit var fadeInBottom: Animation

    fun declareAnim(context: Context) {
        fadeInTop = AnimationUtils.loadAnimation(context, R.anim.fade_in_top)
        fadeInBottom = AnimationUtils.loadAnimation(context, R.anim.fade_in_bottom)
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
   public fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}