package com.choochyemeilin.lamlam.Loans.form

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.transition.Visibility
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form_1.*


class LoanAppForm : AppCompatActivity() {

    private var utils : Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Products")
    private lateinit var arrayList : ArrayList<Products>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form_1)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")

        addCheckbox()

    }

    @SuppressLint("SetTextI18n")
    private fun addCheckbox(){
        arrayList = ArrayList()
        val ll : LinearLayout = findViewById(R.id.loanAppForm1_linearLayout)

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val catItem: Products? = dss.getValue(Products::class.java)
                    if (catItem != null) {
                        arrayList.add(catItem)
                    }
                }
                for (i in arrayList) {
                    var index  = 0
                    val cb = CheckBox(applicationContext)
                    cb.id = index
                    cb.text = "${i.product_name}"
                    cb.setOnClickListener { checkboxAction(cb) }
                    ll.addView(cb)
                    index++
                }
                loanAppForm1_progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkboxAction(cb: CheckBox) {
        utils.toast(applicationContext, "${cb.id}", 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}