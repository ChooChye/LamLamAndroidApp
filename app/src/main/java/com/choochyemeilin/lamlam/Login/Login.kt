package com.choochyemeilin.lamlam.Login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        button_login_signin.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        auth.sendPasswordResetEmail(editText_login_email.text.toString())
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG)
                        .show()
                }
            })
        auth.currentUser?.updatePassword(editTextTextPassword_login_password.text.toString())
            ?.addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password changes successfully", Toast.LENGTH_LONG)
                        .show()
                    finish()
                } else {
                    Toast.makeText(this, "password not changed", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?){

    }

    //Check if a user is logged in or not
    fun getCurrentUser(){
        if(auth.currentUser == null){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }
    }




}