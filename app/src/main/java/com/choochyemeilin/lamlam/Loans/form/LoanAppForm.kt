package com.choochyemeilin.lamlam.Loans.form

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.adapters.LoanFormAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form_1.*
import okhttp3.internal.notifyAll
import java.io.Serializable


class LoanAppForm : AppCompatActivity(), FbCallback {

    private var utils: Utils = Utils
    private var mutableMap : MutableMap<String, Int> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_app_form_1)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Loan Application Form")
        loanform_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0){
                    loanAppForm1_fab.hide()
                }else{
                    loanAppForm1_fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        loanAppForm1_fab.setOnClickListener { nextBtn() }

        getProducts(object : FbCallback {
            override fun push(arr: MutableMap<String, Int>) {
                val initAdapter = LoanFormAdapter(arr, this)

                loanform_rv.adapter = initAdapter
                loanform_rv.layoutManager = LinearLayoutManager(applicationContext)
                loanform_rv.setHasFixedSize(true)
                loanAppForm1_progressBar.visibility = View.GONE

            }

            override fun pushForLoanForm(arr: MutableMap<String, Int>) {
                arr.forEach {
                    val key = it.key
                    val value = it.value

                    if (value == 0)
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
            startActivity(intent)

        }
    }

    private fun getProducts(callback: FbCallback): MutableMap<String, Int> {
        var list : MutableMap<String, Int> = mutableMapOf()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Products")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dss in snapshot.children) {
                    val productName = dss.child("product_name").value.toString()
                    val qty = dss.child("qty").value.toString()
                    list[productName] = qty.toInt()
                }
                callback.push(list)
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