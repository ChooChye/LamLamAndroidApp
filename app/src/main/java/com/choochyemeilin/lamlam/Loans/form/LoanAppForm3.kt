package com.choochyemeilin.lamlam.Loans.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choochyemeilin.lamlam.Loans.Loans
import com.choochyemeilin.lamlam.R
import kotlinx.android.synthetic.main.activity_loan_app_form3.*

class LoanAppForm3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form3)

        loanAppForm3_btn_end.setOnClickListener {
            startActivity(Intent(this, Loans::class.java))
            this.finish()
        }
    }
}