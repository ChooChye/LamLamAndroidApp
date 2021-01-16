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
import androidx.core.view.get
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
    private var passwordLock = false

    //spinner
    lateinit var option:Spinner
    lateinit var result:TextView

    //  get reference
    private var myRef: DatabaseReference = databaseReference.getReference("Staff ID")
    private var userRef: DatabaseReference = databaseReference.getReference("User")
    private var roleRef: DatabaseReference = databaseReference.getReference("Role")
    private var staffRef: DatabaseReference = databaseReference.getReference("Staff ID")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        button_register_reg.setOnClickListener {
            progressBar_reg.visibility = View.VISIBLE
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

            } else if (TextUtils.isEmpty(editTextTextPassword_register_password.text.toString())) {
                editTextTextPassword_register_password.setError("Please enter Password")
                editTextTextPassword_register_password.requestFocus()
                return@setOnClickListener
            }

            if (editTextNumber_register_staffID.length() != 7) {
                editTextNumber_register_staffID.setError("Staff ID must be 7 characters")
                editTextNumber_register_staffID.requestFocus()
                return@setOnClickListener
            }

            if (editTextTextPassword_register_password.length() < 6) {
                editTextTextPassword_register_password.setError("Password must be greater than 5 characters")
                editTextTextPassword_register_password.requestFocus()
                return@setOnClickListener
            }


            // add authentication user
            if (
                editTextTextEmailAddress_register_email.text.trim().toString().isNotEmpty() ||
                editTextTextPassword_register_password.text.trim().toString().isNotEmpty()
            ) {

                staffRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dss in snapshot.children) {
                            //dss.children.forEach {
                                val id = dss.value
                                if (id.toString() == editTextNumber_register_staffID.text.toString()) {
                                    register(
                                        editTextTextEmailAddress_register_email.text.trim().toString(),
                                        editTextTextPassword_register_password.text.trim().toString()
                                    )
                                }
                         //   }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


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

        button_register_cancel.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        imageView_register_password_eye.setOnClickListener {
            passwordLock = !passwordLock
            showPassword(passwordLock)
        }
        showPassword(passwordLock)

        //spinner
        option=findViewById(R.id.spinner_register_retailer_name)as Spinner
        result=findViewById(R.id.textView_register_retailer_address)as TextView

        val options= arrayOf("---Select Retailer Name----","LamLam PV128","LamLam GM Plaza")

        option.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        option.onItemSelectedListener=object :AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val nameResult=options.get(position)
                textView4.text=nameResult

              if(options.get(position).equals("LamLam PV128")){
                  textView_rid.text="1001"
                  result.text="G.20, PV128, Jalan Genting Kelang, Taman Danau Kota, 53100 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur"
              }else if(options.get(position).equals("LamLam GM Plaza")){
                  textView_rid.text="1002"
                  result.text="15, Lorong Haji Taib 5, Chow Kit, 50350 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur"
              }else{
                  textView_rid.text=""
                  result.text=""
              }
         //       result.text=options.get(position)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                result.text=""
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

        }
    }


    //Registers the user
    @RequiresApi(Build.VERSION_CODES.O)
    private fun register(email: String, password: String) {

        utils.closeKeyboard(findViewById(R.id.activity_register))

        //Start progress
        /*val progress: ProgressBar = progressBar_reg
        progress.visibility = View.VISIBLE*/


        //Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this@Register, "Registration Success", Toast.LENGTH_LONG).show()
                    //    val userID="User ID : "
                    val staffID = editTextNumber_register_staffID.text.toString().toInt()
                    val staffName = editText_register_name.text.toString()
                    val staffEmail = editTextTextEmailAddress_register_email.text.toString()
                    val phoneNumber = editTextNumber_register_phoneNo.text.toString().toInt()
                    val pw = editTextTextPassword_register_password.text.toString()
                    val currentUser = auth.currentUser
                    val uid = currentUser?.uid
                    val rname=textView4.text.toString()
                   val rid=textView_rid.text.toString().toInt()


                    if (uid != null) {
                        userRef.child(uid).setValue(

                            Staff(
                                staffID,
                                staffName,
                                staffEmail,
                                phoneNumber,
                                pw,
                                rid
                            )
                        )
                    }


                    startActivity(Intent(this, Login::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@Register,
                        "Registration failed. Please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }

    fun showPassword(isShow: Boolean) {
        if (isShow) {
            editTextTextPassword_register_password.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            imageView_register_password_eye.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        } else {
            editTextTextPassword_register_password.transformationMethod =
                PasswordTransformationMethod.getInstance()
            imageView_register_password_eye.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
        }
        editTextTextPassword_register_password.setSelection(editTextTextPassword_register_password.text.toString().length)
    }


    fun compareStaffID(): Boolean {

        var query: Query = roleRef.child("Admin").orderByChild("Staff ID")
        var q2: Query = roleRef.child("Staff").orderByChild("Staff ID")
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


                var textRegStaffID = editTextNumber_register_staffID.text.toString()


                for (i in dataSnapshot.children) {

                    var id = i.getValue().toString().compareTo(textRegStaffID)
                    if (id.equals(true)) {
                        Toast.makeText(this@Register, "SUCCESS", Toast.LENGTH_LONG).show()
                    } else {
                        editTextNumber_register_staffID.setError("Staff ID is not in the database")
                        editTextNumber_register_staffID.requestFocus()
                    }
                }
            }
        })
        return true
    }


}