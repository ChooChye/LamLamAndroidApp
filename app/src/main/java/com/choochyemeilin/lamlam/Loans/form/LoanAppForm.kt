package com.choochyemeilin.lamlam.Loans.form

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.Loans.adapters.LoanFormAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form_1.*
import kotlinx.android.synthetic.main.loanform_select_product_list.*
import java.io.Serializable


class LoanAppForm : AppCompatActivity(), FbCallback {

    private var utils: Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Products")
    //private var arrayListText: ArrayList<String> = ArrayList()
    private var mutableMap : MutableMap<String, Int> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form_1)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")
        loanAppForm1_fab.setOnClickListener { nextBtn() }

        getPendingLoans(object : FbCallback {
            override fun onCallbackString(arr: ArrayList<String>) {
                val initAdapter = LoanFormAdapter(arr, this)
                loanform_rv.adapter = initAdapter
                loanform_rv.layoutManager = LinearLayoutManager(applicationContext)
                loanform_rv.setHasFixedSize(true)
                loanAppForm1_progressBar.visibility = View.GONE
            }

            override fun push(arr: MutableMap<String, Int>) {
                arr.forEach {
                    val key = it.key
                    val value = it.value

                    if(value == 0)
                        mutableMap.remove(key)
                    else
                        mutableMap[key] = value
                }
            }
        })
    }

    private fun nextBtn() {
        if(mutableMap.isEmpty()){
            utils.toast(applicationContext, "Please select a product to proceed", 0)
        }else{
            val intent = Intent(this, LoanAppForm2::class.java)
            intent.putExtra("map", mutableMap as Serializable)
            utils.log("LoanAppForm = $mutableMap")
            startActivity(intent)

        }
    }

    private fun getPendingLoans(callback: FbCallback): List<String> {
        var list = ArrayList<String>()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Products")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dss in snapshot.children) {
                    val productName = dss.child("product_name").value.toString()
                    list.add(productName)
                }
                callback.onCallbackString(list)
            }

            override fun onCancelled(error: DatabaseError) {
                utils.toast(applicationContext, "An error has occurred #0984 | ${error.message}", 1)
            }

        })
        return list
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}