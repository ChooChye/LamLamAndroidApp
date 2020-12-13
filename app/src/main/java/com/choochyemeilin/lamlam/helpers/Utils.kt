package com.choochyemeilin.lamlam.helpers

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.choochyemeilin.lamlam.R


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

    //Showing the keyboard
    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    //Hiding the keyboard
    private fun View.closeKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}