package com.choochyemeilin.lamlam.Register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.nav_header.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Register : AppCompatActivity() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var utils = Utils
    private var passwordLock=false
  //  private var id=true
    private var myRef: DatabaseReference = databaseReference.getReference("Staff ID")
    private var userRef: DatabaseReference = databaseReference.getReference("User")
    private var roleRef: DatabaseReference = databaseReference.getReference("Role")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        button_register_reg.setOnClickListener{

                if (TextUtils.isEmpty(editTextNumber_register_staffID.text.toString())) {
                    editTextNumber_register_staffID.setError("Please enter Staff ID")
                    editTextNumber_register_staffID.requestFocus()
                    return@setOnClickListener

                } else if (TextUtils.isEmpty(editText_register_name.text.toString())) {
                    editText_register_name.setError("Please enter Name")
                    editText_register_name.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(editTextTextEmailAddress_register_email.text.toString())) {
                    editTextTextEmailAddress_register_email.setError("Please enter Email")
                    editTextTextEmailAddress_register_email.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(editTextNumber_register_phoneNo.text.toString())) {
                    editTextNumber_register_phoneNo.setError("Please enter Phone Number")
                    editTextNumber_register_phoneNo.requestFocus()
                    return@setOnClickListener

                }else if (TextUtils.isEmpty(editTextTextPassword_register_password.text.toString())) {
                    editTextTextPassword_register_password.setError("Please enter Password")
                    editTextTextPassword_register_password.requestFocus()
                    return@setOnClickListener
                }

           if (editTextNumber_register_staffID.length()!=7){
               editTextNumber_register_staffID.setError("Staff ID must be 7 characters")
               editTextNumber_register_staffID.requestFocus()
               return@setOnClickListener
             }

            if (editTextTextPassword_register_password.length()<6){
                editTextTextPassword_register_password.setError("Password must be greater than 5 characters")
                editTextTextPassword_register_password.requestFocus()
                return@setOnClickListener
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

 /*               if(compareStaffID()) {
                    Toast.makeText(this@Register, "ID NOT IN THE DATABASE", Toast.LENGTH_LONG).show()
                }else{
                    register(
                        editTextTextEmailAddress_register_email.text.trim().toString(),
                        editTextTextPassword_register_password.text.trim().toString()
                    )
                }


  */
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
    private fun register(email: String, password: String){

        utils.closeKeyboard(findViewById(R.id.activity_register))

        //Start progress
        val progress: ProgressBar = progressBar_reg
        progress.visibility = View.VISIBLE


        //Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                //    val userID="User ID : "
                    val staffID=editTextNumber_register_staffID.text.toString().toInt()
                    val staffName=editText_register_name.text.toString()
                    val  staffEmail=editTextTextEmailAddress_register_email.text.toString()
                    val phoneNumber=editTextNumber_register_phoneNo.text.toString().toInt()
                    val pw=editTextTextPassword_register_password.text.toString()
                    val currentUser=auth.currentUser
                    val uid = currentUser?.uid


                    if (uid != null) {
                        userRef.child(uid).setValue(

                            Staff(
                                staffID,
                                staffName,
                                staffEmail,
                                phoneNumber,
                                pw
                            )
                        )
                    }

                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }else{
                    Toast.makeText(
                        this@Register,
                        "Registration failed. Please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }

     fun showPassword(isShow: Boolean){
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


    fun compareStaffID():Boolean{

        var query : Query =roleRef.child("Admin").orderByChild("Staff ID")
        var q2:Query=roleRef.child("Staff").orderByChild("Staff ID")
   //     val textRegStaffID = findViewById<EditText>(R.id.editTextNumber_register_staffID)


        //Get Data
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                utils.log("$error")
                Toast.makeText(this@Register, "ERROR", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

             //   val i=dataSnapshot.getValue().toString()
            //    i.equals(textRegStaffID)


               var textRegStaffID=editTextNumber_register_staffID.text.toString()


                for (i in dataSnapshot.children){

                    var id=i.getValue().toString().compareTo(textRegStaffID)
                    if(id.equals(true)){
                        Toast.makeText(this@Register, "SUCCESS", Toast.LENGTH_LONG).show()
                    }else{
                        editTextNumber_register_staffID.setError("Staff ID is not in the database")
                        editTextNumber_register_staffID.requestFocus()
                    }

                  //  textRegStaffID.equals(id)
/*
                  if (!id.equals(textRegStaffID)){
                     editTextNumber_register_staffID.setError("Staff ID is not in the database")
                     editTextNumber_register_staffID.requestFocus()

                  }
      */           }



            }
        })
        return true
    }


}