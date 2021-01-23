package com.choochyemeilin.lamlam.Loans.form

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.Loans.Classes.SelectedProducts
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Retailers
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form2.*
import java.text.DecimalFormat


private const val KEY = "map"

class LoanAppForm2 : AppCompatActivity() {

    private var mutableList: MutableMap<String, Int> = mutableMapOf()
    private var loanID: Int = genLoanID()
    private var rID: Int = 0
    var staffID: Int? = 0

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Loans")
    private var prodRef: DatabaseReference = database.getReference("Products")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")
        loanAppForm2_tv_ref.text = "REFERENCE NO: #${loanID}"
        val b = this.intent.getSerializableExtra(KEY)
        mutableList = b as MutableMap<String, Int>

        Utils.getStaffID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                super.onCallbackGetUserID(uid)
                staffID = uid
            }
        })
        setupSelectedProducts()
        loanAppForm_btn_applyNow.setOnClickListener { applyNow() }

    }


    //Apply now action

    private fun applyNow() {
        val loan_id = loanID

        val products = mutableList
        val status = "pending"

        var df: DecimalFormat? = DecimalFormat("00")

        val current: org.joda.time.LocalDateTime = org.joda.time.LocalDateTime.now()
        val hour = current.hourOfDay
        val min = current.minuteOfHour
        val sec = current.secondOfMinute
        val formattedTime = "$hour:$min:$sec"

        val day = current.dayOfMonth
        val month = df?.format(current.monthOfYear)
        val year = current.year

        val formattedDate = "$year-$month-$day"
        val loanDate = "$formattedDate $formattedTime"

        //Create Class
        val loanApplication = LoanApplication(loan_id, loanDate, status, products, staffID!!, rID)

        myRef.child(formattedDate).child(formattedTime).setValue(loanApplication)
            .addOnSuccessListener {

                startActivity(Intent(this, LoanAppForm3::class.java))
                this.finish()
            }
    }

    //Initialize
    @SuppressLint("SetTextI18n")
    private fun setupSelectedProducts() {
        val ll: LinearLayout = findViewById(R.id.loanAppForm2_linearLayout)
        var index = 0
        mutableList.forEach {
            val key = it.key
            val value = it.value

            //Populate on view
            val tv = TextView(this)
            tv.id = index
            tv.text = "$key ($value)"
            ll.addView(tv)
            index++
        }
        Utils.getRetailerInfo(object : FbCallback {
            override fun onCallbackRetailer(arr: ArrayList<Retailers>) {
                super.onCallbackRetailer(arr)
                rID = arr[0].rID!!
                loanAppform2_tv_retailerName.text = "${arr[0].rName} #${arr[0].rID}"
                loanAppform2_tv_retailerAddress.text = arr[0].rAddress
            }
        })
        loanAppForm2_progressBar.visibility = View.GONE
    }

    //Generate Loan ID Number
    private fun genLoanID(): Int {
        return (10000..99999).random()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    override fun finish() {
        super.finish()
    }
}