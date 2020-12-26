package com.choochyemeilin.lamlam.Loans.form

import android.R.attr.key
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form_1.*
import java.lang.Exception


class LoanAppForm : AppCompatActivity() {

    private var utils : Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Products")
    private lateinit var arrayListId : ArrayList<Int>
    private lateinit var arrayListText : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form_1)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")
        arrayListId = ArrayList()
        arrayListText = ArrayList()
        loanAppForm1_fab.setOnClickListener { nextBtn() }
        addCheckbox()

    }

    private fun nextBtn() {
        if(arrayListText.size > 0){
            val b = Bundle()
            b.putStringArrayList("arrayListText", arrayListText)
            val intent = Intent(this, LoanAppForm2::class.java)
            intent.putExtras(b)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addCheckbox(){
        val ll : LinearLayout = findViewById(R.id.loanAppForm1_linearLayout)
        var index  = 0

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val catItem: Products? = dss.getValue(Products::class.java)
                    if (catItem != null) {
                        val cb = CheckBox(applicationContext)
                        cb.id = index
                        cb.text = "${catItem.product_name}"
                        cb.setOnCheckedChangeListener { compoundButton, b ->
                            checkboxAction(cb)
                        }
                        ll.addView(cb)
                        index++
                    }
                }
                loanAppForm1_progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        /*for (i in 0..20) {
            var index = 0
            val cb = CheckBox(applicationContext)
            cb.id = index
            cb.text = "${i}"
            cb.setOnClickListener { checkboxAction(cb) }
            ll.addView(cb)
            index++
        }
        loanAppForm1_progressBar.visibility = View.GONE*/
    }

    private fun checkboxAction(cb: CheckBox) {
        try {
            if(cb.isChecked){
                arrayListId.add(cb.id)
                arrayListText.add(cb.text.toString())
                loanAppForm1_fab.isEnabled = true
            }else{
                arrayListId.removeAt(cb.id)
                arrayListText.remove(cb.text.toString())
            }

            if(arrayListText.size == 0){
                loanAppForm1_fab.isEnabled = false
            }
        }catch (e: Exception){
            //Stop the index bound exception
            //utils.toast(applicationContext, "An error has occurred Error #8905 | $e", 1)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}