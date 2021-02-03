package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form_1.*
import kotlinx.android.synthetic.main.activity_my_stocks.*
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.my_stocks_list.*
import kotlinx.android.synthetic.main.my_stocks_list.view.*

class MyStocks : AppCompatActivity() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var utils: Utils = Utils
    private var mutableList: MutableMap<String, Int> = mutableMapOf()
    private var rList: MutableMap<String, Int> = mutableMapOf()
    private var loanDatesArr: ArrayList<String> = arrayListOf()
    private var staffID: Int? = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stocks)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "My Stocks"

        //  arrayList = ArrayList()
        list_view_recycle.setHasFixedSize(true)


        Utils.getStaffID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                super.onCallbackGetUserID(uid)
                staffID = uid

            }
        })



        getData(object : FbCallback {
            override fun pushLoanDate(arr: MutableMap<String, Int>, loanDate: ArrayList<String>, oldestDate : String) {
                super.push(arr)

                rList = arr
                list_view_recycle.adapter = MyStocksAdapter(arr, loanDate, oldestDate)
                list_view_recycle.layoutManager = LinearLayoutManager(applicationContext)


                if (arr.isEmpty()) {
                    utils.toast(this@MyStocks, "No result", 1)
                }
            }
        })

    }

    private fun getData(callback: FbCallback) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mutableList.clear()
                    var oldestDate = ""
                    for (dss in snapshot.children) {
                        dss.children.forEachIndexed { index, it ->
                            val dbSID = it.child("staffID").value.toString().toInt()
                            val status = it.child("status").value.toString()
                            val loanDate = it.child("loanDate").value.toString()

                            if (staffID == dbSID) {
                                if (status.toUpperCase() == "APPROVED") {
                                    val product = it.child("productName")
                                    product.children.forEach {
                                        val key = it.key.toString()
                                        val qty = it.value.toString().toInt()
                                        if (mutableList.containsKey(key)) {
                                            val oldValue = mutableList[key].toString().toInt()
                                            mutableList[key] = oldValue + qty

                                        } else {
                                            if (index == 1){
                                                oldestDate = loanDate
                                            }
                                            loanDatesArr.add(loanDate)
                                            mutableList[key] = qty

                                        }

                                    }

                                }
                            }

                        }
                    }
                    callback.pushLoanDate(mutableList, loanDatesArr, oldestDate)
                }

                override fun onCancelled(error: DatabaseError) {
                    Utils.toast(applicationContext, error.message, 1)
                }

            })


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}