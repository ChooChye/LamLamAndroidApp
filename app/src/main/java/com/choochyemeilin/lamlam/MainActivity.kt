package com.choochyemeilin.lamlam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.Register.Register
import com.choochyemeilin.lamlam.ReturnItems.ReturnItems
import com.choochyemeilin.lamlam.Search.Search
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var utils = Utils
    private var TIME_OUT: Long = 2000
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide() //Remove Action Bar

        //Declare Var
        var logo    = findViewById<ImageView>(R.id.splash_screen_logo)
        var pBar    = findViewById<ProgressBar>(R.id.splash_screen_progressBar)

        auth = FirebaseAuth.getInstance()
        val currentUser=auth.currentUser

        //Declare Animation
        utils.declareAnim(this)

        logo.startAnimation(utils.fadeInTop)
        pBar.startAnimation(utils.fadeInBottom)

        //start activity
        Handler().postDelayed(Runnable {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()

            if (currentUser != null) {
                // User is signed in
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            } else {
                // No user is signed in
               // Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show()
            }



        }, TIME_OUT)
    }
}