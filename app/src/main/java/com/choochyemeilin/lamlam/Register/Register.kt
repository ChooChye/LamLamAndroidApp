package com.choochyemeilin.lamlam.Register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.choochyemeilin.lamlam.Login.ForgotPasswordActivity
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.HashMap

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null

  //  private var fStore         : FirebaseFirestore = FirebaseFirestore.getInstance()
   // private var fAuth          : FirebaseAuth = FirebaseAuth.getInstance()
  //  private var fData          : FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("profile")

        register()

        button_register_cancel.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }

    //Registers the user
    private fun register(){
        closeKeyBoard()

        button_register_reg.setOnClickListener {
            if (TextUtils.isEmpty(editTextNumber_register_staffID.text.toString())){
                editTextNumber_register_staffID.setError("Please enter staff ID")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(editText_register_name.text.toString())){
                editTextNumber_register_staffID.setError("Please enter name")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(editTextTextEmailAddress_register_email.text.toString())){
                editTextNumber_register_staffID.setError("Please enter email")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(editTextNumber_register_phoneNo.text.toString())){
                editTextNumber_register_staffID.setError("Please enter phone number")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(editTextTextPassword_register_password.text.toString())){
                editTextNumber_register_staffID.setError("Please enter password")
                return@setOnClickListener
            }
        }

        //Start progress
        val progress: ProgressBar = progressBar_reg
        progress.visibility = View.VISIBLE

        //Firebase
        auth.createUserWithEmailAndPassword(editTextNumber_register_staffID.text.toString(),editTextTextPassword_register_password.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val currentUser=auth.currentUser
                    val currentUserDb=databaseReference?.child(currentUser?.uid!!)
                    currentUserDb?.child("staffID")?.setValue(editTextNumber_register_staffID.text.toString())
                    currentUserDb?.child("name")?.setValue(editTextNumber_register_staffID.text.toString())


                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }else{
                    Toast.makeText(this@Register, "Registration failed. Please try again", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}