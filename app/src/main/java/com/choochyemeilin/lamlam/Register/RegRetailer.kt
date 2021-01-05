package com.choochyemeilin.lamlam.Register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reg_retailer.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.progressBar_reg

class RegRetailer : AppCompatActivity() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var utils = Utils
    private var userRef: DatabaseReference = databaseReference.getReference("User")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_retailer)

        button_reg_retailer.setOnClickListener {
            register()
        }
    }

    //Registers the user
    @RequiresApi(Build.VERSION_CODES.O)
    private fun register() {

        //Start progress
        val progress: ProgressBar = progressBar_reg
        progress.visibility = View.VISIBLE


        //Firebase


                    Toast.makeText(this, "Retailer Registration Success", Toast.LENGTH_LONG).show()


                    val retailerName=editText_reg_retailer_name.text.toString()

                    val retailerAddress=editText_reg_retailer_address.text.toString()
                    val currentUser = auth.currentUser
                    val uid = currentUser?.uid


                    if (uid != null) {
                        userRef.child(uid).setValue(

                            Staff(
                               retailerName,
                                retailerAddress
                            )
                        )
                    }


                    startActivity(Intent(this, Login::class.java))
                    finish()


    }
}