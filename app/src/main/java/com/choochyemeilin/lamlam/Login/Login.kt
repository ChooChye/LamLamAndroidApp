package com.choochyemeilin.lamlam.Login

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Register.Register
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.StringBuilder
import java.util.regex.Matcher

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var utils = Utils
    private var passwordLock=false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        utils.closeKeyboard(findViewById(R.id.activity_login))

     //   val currentUser=auth.currentUser
        textView_login_register.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
        }

        button_login_signin.setOnClickListener{
            login()
        }

        text_login_forgotpw.setOnClickListener{
            val builder:AlertDialog.Builder=AlertDialog.Builder(this)
            builder.setTitle("FORGOT PASSWORD: \n Please enter your email")
            val view:View=layoutInflater.inflate(R.layout.activity_forgot_password,null)
            val email:EditText=view.findViewById<EditText>(R.id.editText_login_forgotpw)
            builder.setView(view)
            builder.setPositiveButton("Reset",DialogInterface.OnClickListener{ _, _ ->
                forgotPassword(email)
            })
            builder.setNegativeButton("Close",DialogInterface.OnClickListener { _, _ -> })
            builder.show()
        }

        imageView_login_password_eye.setOnClickListener{
            passwordLock=!passwordLock
            showPassword(passwordLock)
        }
        showPassword(passwordLock)

    }

    fun login(){
        val email:String=editText_login_email.text.toString()
        val password:String=editTextTextPassword_login_password.text.toString()
        val currentUser=auth.currentUser

        if(editTextTextPassword_login_password.length()<6){
            editTextTextPassword_login_password.setError("Password must be greater than 5 characters")
        }

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(editText_login_email.text.toString())){
                editText_login_email.setError("Please enter Email")
                editText_login_email.requestFocus()
            }else if(TextUtils.isEmpty(editTextTextPassword_login_password.text.toString())){
                editTextTextPassword_login_password.setError("Please enter Password")
                editTextTextPassword_login_password.requestFocus()
            }

            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
        } else{

            //Sign IN
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()

                }
            })
        }
    }

    private fun forgotPassword(email:EditText){
        if(email.text.toString().isEmpty()){
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }

        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Email Sent",Toast.LENGTH_SHORT).show()
                }
            }
    }

     fun showPassword(isShow:Boolean){
        if(isShow){
            editTextTextPassword_login_password.transformationMethod=HideReturnsTransformationMethod.getInstance()
            imageView_login_password_eye.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }else{
            editTextTextPassword_login_password.transformationMethod=PasswordTransformationMethod.getInstance()
            imageView_login_password_eye.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
        }
        editTextTextPassword_login_password.setSelection(editTextTextPassword_login_password.text.toString().length)
    }



}



