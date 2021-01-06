package com.choochyemeilin.lamlam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.Reports.Reports
import com.choochyemeilin.lamlam.helpers.Utils


class MainActivity : AppCompatActivity() {
    private var utils = Utils
    private var TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide() //Remove Action Bar

        //Declare Var
        val logo    = findViewById<ImageView>(R.id.splash_screen_logo)
        val pBar    = findViewById<ProgressBar>(R.id.splash_screen_progressBar)


        //Declare Animation
        utils.declareAnim(this)

        logo.startAnimation(utils.fadeInTop)
        pBar.startAnimation(utils.fadeInBottom)

        //start activity
        Handler().postDelayed( {
            if(Utils.checkUserAuth()){
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_OUT)
    }
}