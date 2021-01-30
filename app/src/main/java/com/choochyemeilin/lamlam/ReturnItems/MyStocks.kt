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

     lateinit var mRecyclerView: RecyclerView

     var database: FirebaseDatabase = FirebaseDatabase.getInstance()
     private lateinit var arrayList: ArrayList<Products>
     private var utils : Utils = Utils
     private var mutableList: MutableMap<String, Int> = mutableMapOf()
     private var rList: MutableMap<String, Int> = mutableMapOf()
     private lateinit var testingArray:ArrayList<String>
     private var staffID : Int? = 0
     private var ldate="testing"

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
            override fun push(arr: MutableMap<String, Int>){
                super.push(arr)
      //          super.pushLoanDate(ldate)
                rList = arr
                list_view_recycle.adapter = MyStocksAdapter(arr,ldate)
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
                     for (dss in snapshot.children) {
                         dss.children.forEach {
                             val dbSID = it.child("staffID").value.toString().toInt()
                             val status = it.child("status").value.toString()
                             val loanDate = it.child("loanDate").value.toString()
                             val testingArray = arrayOf(loanDate)

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
                                             mutableList[key] = qty
                                             ldate= testingArray[0]
                                        //     ldate=loanDate

                                         }

                                     }

                                 }
                             }

                         }
                     }
                     callback.push(mutableList )
                //     callback.pushLoanDate(ldate)
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