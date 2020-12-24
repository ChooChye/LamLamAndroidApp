package com.choochyemeilin.lamlam.Loans.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choochyemeilin.lamlam.R

class LoanAppForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}