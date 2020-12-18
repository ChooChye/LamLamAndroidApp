package com.choochyemeilin.lamlam.Register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.HashMap

class Register : AppCompatActivity() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var utils = Utils
    private var passwordLock=false
    private var myRef: DatabaseReference = databaseReference.getReference("User")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register_reg.setOnClickListener{

                if (TextUtils.isEmpty(editTextNumber_register_staffID.text.toString())) {
                    editTextNumber_register_staffID.setError("Please enter Staff ID")

                } else if (TextUtils.isEmpty(editText_register_name.text.toString())) {
                    editText_register_name.setError("Please enter Name")

                } else if (TextUtils.isEmpty(editTextTextEmailAddress_register_email.text.toString())) {
                    editTextTextEmailAddress_register_email.setError("Please enter Email")

                } else if (TextUtils.isEmpty(editTextNumber_register_phoneNo.text.toString())) {
                    editTextNumber_register_phoneNo.setError("Please enter Phone Number")

                } else if (TextUtils.isEmpty(editTextTextPassword_register_password.text.toString())) {
                    editTextTextPassword_register_password.setError("Please enter Password")
                }


            // add authentication user
                if (
                    editTextTextEmailAddress_register_email.text.trim().toString().isNotEmpty() ||
                    editTextTextPassword_register_password.text.trim().toString().isNotEmpty()
                ) {

                    register(
                        editTextTextEmailAddress_register_email.text.trim().toString(),
                        editTextTextPassword_register_password.text.trim().toString()
                    )
                }

        }

        button_register_cancel.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        imageView_register_password_eye.setOnClickListener{
            passwordLock=!passwordLock
            showPassword(passwordLock)
        }
        showPassword(passwordLock)
    }


    //Registers the user
    @RequiresApi(Build.VERSION_CODES.O)
    private fun register(email:String, password:String){

      //  utils.closeKeyboard(findViewById(R.id.activity_register))


        //Start progress
        val progress: ProgressBar = progressBar_reg
        progress.visibility = View.VISIBLE

     //   val email=findViewById<EditText>(R.id.editTextTextEmailAddress_register_email)
     //   val password=findViewById<EditText>(R.id.editTextTextPassword_register_password)

        //Firebase
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val currentUser=auth.currentUser
                    val userID="User ID : "
                    val staffID=editTextNumber_register_staffID.text.toString().toInt()
                    val staffName=editText_register_name.text.toString()
                    val  staffEmail=editTextTextEmailAddress_register_email.text.toString()
                    val phoneNo=editTextNumber_register_phoneNo.text.toString().toInt()
                    val pw=editTextTextPassword_register_password.text.toString()

                   // val currentUserDb=databaseReference.reference.child(currentUser?.uid!!)
                 //   val currentUserDb=databaseReference.reference.child("User").child(currentUser?.uid!!)
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //yyyy-MM-dd HH:mm:ss.SSS
                    val formattedDate = current.format(formatter)
                    myRef.child(formattedDate).child(userID).child(staffID.toString()).setValue(Staff(staffName,staffEmail,phoneNo,pw))



/*
                    currentUserDb?.child("Staff ID")?.setValue(editTextNumber_register_staffID.text.toString())
                    currentUserDb?.child("Name")?.setValue(editText_register_name.text.toString())
                    currentUserDb?.child("Email")?.setValue(editTextTextEmailAddress_register_email.text.toString())
                    currentUserDb?.child("Phone Number")?.setValue(editTextNumber_register_phoneNo.text.toString())
                    currentUserDb?.child("Password")?.setValue(editTextTextPassword_register_password.text.toString())

 */

                 //   database.child(staffID.toString()).setValue(Staff(staffName,staffEmail,phoneNumber,pw))
                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }else{
                    Toast.makeText(this@Register, "Registration failed. Please try again", Toast.LENGTH_LONG).show()
                }
            }
    }

     fun showPassword(isShow:Boolean){
        if(isShow){
            editTextTextPassword_register_password.transformationMethod=
                HideReturnsTransformationMethod.getInstance()
            imageView_register_password_eye.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }else{
            editTextTextPassword_register_password.transformationMethod= PasswordTransformationMethod.getInstance()
            imageView_register_password_eye.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
        }
         editTextTextPassword_register_password.setSelection(editTextTextPassword_register_password.text.toString().length)
    }

}