package com.choochyemeilin.lamlam.Loans.form

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.Loans.Loans
import com.choochyemeilin.lamlam.R
import kotlinx.android.synthetic.main.activity_loan_app_form3.*

class LoanAppForm3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form3)

        loanAppForm3_btn_end.setOnClickListener {
            val i = Intent(this, Loans::class.java)
            startActivity(i)
            this.finish()
        }
    }
}