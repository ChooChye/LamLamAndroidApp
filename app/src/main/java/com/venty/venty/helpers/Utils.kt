package com.venty.venty.helpers

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.venty.venty.R


object Utils {

    lateinit var fadeInTop : Animation
    lateinit var fadeInBottom : Animation

    fun declareAnim(context: Context){
        fadeInTop       = AnimationUtils.loadAnimation(context, R.anim.fade_in_top)
        fadeInBottom    = AnimationUtils.loadAnimation(context, R.anim.fade_in_bottom)
    }
}