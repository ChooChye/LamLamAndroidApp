package com.choochyemeilin.lamlam.Loans.form

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.Loans.Classes.SelectedProducts
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_loan_app_form2.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


private const val KEY = "map"

class LoanAppForm2 : AppCompatActivity() {

    private var utils: Utils = Utils
    private var mutableList: MutableMap<String, Int> = mutableMapOf()
    private var arrayList: ArrayList<SelectedProducts> = ArrayList()
    private var loanID: Int = genLoanID()

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Loans")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")
        loanAppForm2_tv_ref.text = "REFERENCE NO: #${loanID}"
        val b = this.intent.getSerializableExtra(KEY)
        mutableList = b as MutableMap<String, Int>


        setupSelectedProducts()
        loanAppForm_btn_applyNow.setOnClickListener { applyNow() }

    }


    //Apply now action
    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyNow() {
        val loan_id = loanID
        val loanDate = utils.now()
        val products = mutableList
        val status = "pending"

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //yyyy-MM-dd HH:mm:ss.SSS
        val formatter2 = DateTimeFormatter.ofPattern("HH:mm::ss")
        val formattedDate = current.format(formatter)
        val formattedTime = current.format(formatter2)

        //Create Class
        val loanApplication = LoanApplication(loan_id, loanDate, status, products)
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
        utils.log("LoanAppForm2 = $mutableList")
        mutableList.forEach {
            val key = it.key
            val value = it.value

            //Populate on view
            val tv = TextView(this)
            tv.id = index
            tv.text = "$key ($value)"
            ll.addView(tv)
            arrayList.add(SelectedProducts(index, key, value))
            index++
        }
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
}