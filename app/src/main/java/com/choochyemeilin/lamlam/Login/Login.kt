package com.choochyemeilin.lamlam.Login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var firstTimeUser=true
    private var fileUri: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val currentUser=auth.currentUser

        button_login_signin.setOnClickListener{
            if(currentUser!=null){
                startActivity(Intent(this,Home::class.java))
                finish()
            }
        }

        text_login_forgotpw.setOnClickListener{
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
            finish()
        }



    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?){

    }






}