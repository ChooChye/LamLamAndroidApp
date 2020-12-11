package com.choochyemeilin.lamlam.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.choochyemeilin.lamlam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("profile")

        register()

    }

    private fun register(){
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

        auth.createUserWithEmailAndPassword(editTextNumber_register_staffID.text.toString(),editTextTextPassword_register_password.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val currentUser=auth.currentUser
                    val currentUserDb=databaseReference?.child(currentUser?.uid!!)
                    currentUserDb?.child("staffID")?.setValue(editTextNumber_register_staffID.text.toString())
                    currentUserDb?.child("name")?.setValue(editTextNumber_register_staffID.text.toString())

                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this@Register, "Registration failed. Please try again", Toast.LENGTH_LONG).show()
                }
            }
    }
}