package com.choochyemeilin.lamlam.Loans.form

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.activity_loan_app_form2.*


private const val KEY = "arrayListText"
class LoanAppForm2 : AppCompatActivity() {

    private var utils : Utils = Utils
    private var array: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")
        loanAppForm2_tv_ref.text = "REFERENCE NO: #${genLoanID()}"
        val b = this.intent.extras
        array = b!!.getStringArrayList(KEY)!!
        setupSelectedProducts()
        loanAppForm_btn_applyNow.setOnClickListener { applyNow() }

    }

    private fun applyNow() {
        TODO("Not yet implemented")
    }

    private fun setupSelectedProducts(){
        val ll : LinearLayout = findViewById(R.id.loanAppForm2_linearLayout)
        var index = 0
        for (i in array) {
            val tv = TextView(this)
            tv.id = index
            tv.text = "${array[index]}"
            ll.addView(tv)
            index++
        }
        loanAppForm2_progressBar.visibility = View.GONE
    }

    private fun genLoanID() : Int{
        return (10000..99999).random()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}