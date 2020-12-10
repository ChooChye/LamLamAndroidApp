package com.choochyemeilin.lamlam.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        button_login_signin.setOnClickListener {
            SignUpUser()
        }

    }

    private fun SignUpUser(){
        if(editText_login_email.text.toString().isEmpty()){
            editText_login_email.error="Please enter email"
            editText_login_email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(editText_login_email.text.toString()).matches()){
            editText_login_email.error="Please enter valid email"
            editText_login_email.requestFocus()
        }

        if(editTextTextPassword_login_password.text.toString().isEmpty()){
            editTextTextPassword_login_password.error="Please enter password"
            editTextTextPassword_login_password.requestFocus()
            return
        }



        auth.createUserWithEmailAndPassword(editText_login_email.text.toString(), editTextTextPassword_login_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                } else {
                 Toast.makeText(baseContext,"Sign up failed. Please try again....", Toast.LENGTH_SHORT).show()
                }

            }
    }

}