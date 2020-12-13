package com.choochyemeilin.lamlam.Register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.navigation.findNavController
import com.choochyemeilin.lamlam.Login.ForgotPasswordActivity
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.HashMap

class Register : AppCompatActivity() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var utils = Utils

   // var database= FirebaseDatabase.getInstance().reference

  //  private var fStore         : FirebaseFirestore = FirebaseFirestore.getInstance()
   // private var fAuth          : FirebaseAuth = FirebaseAuth.getInstance()
  //  private var fData          : FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

       // databaseReference.reference.child("profile")
        button_register_reg.setOnClickListener{
            if (TextUtils.isEmpty(editTextNumber_register_staffID.text.toString())){
                editTextNumber_register_staffID.setError("Please enter Staff ID")

            }else if(TextUtils.isEmpty(editText_register_name.text.toString())){
                editText_register_name.setError("Please enter Name")

            }else if(TextUtils.isEmpty(editTextTextEmailAddress_register_email.text.toString())){
                editTextTextEmailAddress_register_email.setError("Please enter Email")

            }else if(TextUtils.isEmpty(editTextNumber_register_phoneNo.text.toString())){
                editTextNumber_register_phoneNo.setError("Please enter Phone Number")

            }else if(TextUtils.isEmpty(editTextTextPassword_register_password.text.toString())){
                editTextTextPassword_register_password.setError("Please enter Password")

            }

            // add authentication user
            if(editTextTextEmailAddress_register_email.text.trim().toString().isNotEmpty()||
                editTextTextPassword_register_password.text.trim().toString().isNotEmpty()){
                register(editTextTextEmailAddress_register_email.text.trim().toString(),
                    editTextTextPassword_register_password.text.trim().toString())
            }

        }

        button_register_cancel.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }



    //Registers the user
    private fun register(email:String, password:String){
        /*
        utils.closeKeyboard()
         */

        closeKeyBoard()

        /*
        var staffID=editTextNumber_register_staffID.toString().toInt()
        var staffName=editText_register_name.toString()
        var staffEmail=editTextTextEmailAddress_register_email.toString()
        var phoneNumber=editTextNumber_register_phoneNo.toString().toInt()
        var pw=editTextTextPassword_register_password.toString()

         */

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
                   // val currentUserDb=databaseReference.reference.child(currentUser?.uid!!)
                   //  val currentUserDb=databaseReference.reference.child("User")
                 // val   currentUserDb=databaseReference.reference.child(editTextNumber_register_staffID.text.toString())
                    val currentUserDb=databaseReference.reference.child("User").child(currentUser?.uid!!)

                    currentUserDb?.child("Staff ID")?.setValue(editTextNumber_register_staffID.text.toString())
                    currentUserDb?.child("Name")?.setValue(editText_register_name.text.toString())
                    currentUserDb?.child("Email")?.setValue(editTextTextEmailAddress_register_email.text.toString())
                    currentUserDb?.child("Phone Number")?.setValue(editTextNumber_register_phoneNo.text.toString())
                    currentUserDb?.child("Password")?.setValue(editTextTextPassword_register_password.text.toString())



                 //   database.child(staffID.toString()).setValue(Staff(staffName,staffEmail,phoneNumber,pw))
                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }else{
                    Toast.makeText(this@Register, "Registration failed. Please try again", Toast.LENGTH_LONG).show()
                }
            }
    }

    //Hiding the keyboard
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}