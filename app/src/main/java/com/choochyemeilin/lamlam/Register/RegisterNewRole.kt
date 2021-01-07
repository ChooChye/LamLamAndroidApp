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
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_new_role.*

class RegisterNewRole : AppCompatActivity() {


    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = databaseReference.getReference("Retailers")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_role)
        
        register()
    }

    //Registers the user

    private fun register(){

        var num=0
   //     var no=0

        val staffID=editTextNumber_StaffID.text.toString()

        button.setOnClickListener {
            num++
            myRef.child((num).toString())
                .child("retailerAdrress").setValue(editTextNumber_StaffID.text.toString())
            myRef.child((num).toString()).child("retailerName").setValue(editTextTextPersonName_ROLE.text.toString())
            myRef.child((num).toString()).child("retailerID").setValue(editTextNumber_ID.text.toString().toInt())
            Toast.makeText(this, " Success", Toast.LENGTH_LONG).show()
        /*

            if(editTextTextPersonName_ROLE.text.toString()=="admin"){

                    num++
                    myRef.child("Admin").child((num).toString()).child("Staff ID").setValue(editTextNumber_StaffID.text.toString().toInt())
                    Toast.makeText(this, "Admin Success", Toast.LENGTH_LONG).show()


            }else if (editTextTextPersonName_ROLE.text.toString()=="staff"){

                no++
                myRef.child("Staff").child((no).toString()).child("Staff ID").setValue(editTextNumber_StaffID.text.toString().toInt())
                Toast.makeText(this, " Staff Success", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, " ERROR", Toast.LENGTH_LONG).show()
            }


         */

        }



    }
}