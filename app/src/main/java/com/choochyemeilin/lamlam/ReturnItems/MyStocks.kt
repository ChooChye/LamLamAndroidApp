 package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.adapters.LoanFormAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Reports.adapters.ReportAdapter
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_loan_app_form_1.*
import kotlinx.android.synthetic.main.activity_my_stocks.*
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.my_stocks_list.*
import kotlinx.android.synthetic.main.my_stocks_list.view.*

 class MyStocks : AppCompatActivity() {

     lateinit var mRecyclerView: RecyclerView

     var database: FirebaseDatabase = FirebaseDatabase.getInstance()
     private var myRef: DatabaseReference = database.getReference("Products")
     private lateinit var arrayList: ArrayList<Products>
     private var utils : Utils = Utils
     private lateinit var auth: FirebaseAuth
     private var mutableList: MutableMap<String, Int> = mutableMapOf()
     private var rList: MutableMap<String, Int> = mutableMapOf()

     private var staffID : Int? = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stocks)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "My Stocks"

      //  arrayList = ArrayList()
        list_view_recycle.setHasFixedSize(true)

      //  mRecyclerView=findViewById(R.id.list_view_recycle)


        Utils.getStaffID(object : FbCallback{
            override fun onCallbackGetUserID(uid: Int) {
                super.onCallbackGetUserID(uid)
                staffID = uid

            }
        })


      // StocksRecyclerView()

        getData(object : FbCallback {
            override fun push(arr: MutableMap<String, Int>) {
                super.push(arr)
                rList = arr
                list_view_recycle.adapter = MyStocksAdapter(arr)
                list_view_recycle.layoutManager = LinearLayoutManager(applicationContext)



                if (arr.isEmpty()) {
                 //   textView_stock_date.text = "No results found"
                    utils.toast(this@MyStocks,"No result",1)
                }
            }
        })



    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun StocksRecyclerView(){

       /* var loansRef: DatabaseReference = database.getReference("Loans")

        loansRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val productItem: Products? = dss.getValue(Products::class.java)
                    if (productItem != null){

                        val myAdapter = MyStocksAdapter(applicationContext, arrayList)

                        list_view_recycle.adapter = myAdapter
                        list_view_recycle.layoutManager = LinearLayoutManager(
                            applicationContext, LinearLayoutManager.VERTICAL,
                            false
                        )
                        myAdapter.notifyDataSetChanged()

             *//*           dss.children.forEach {
                            val status = it.child("status").value
                            val sid = it.child("staffID").value
                            if (getStaffID().equals(sid)) {
                                if (status.toString() == "pending") {
                                    val loopName = it.child("productName")

                                    loopName.children.forEach {
                                        val k = it.key
                                        val quantity = it.value


                                        textView_stock_name.text = k.toString()
                                        textView_stock_qty.text = quantity.toString()

                                        val myAdapter = MyStocksAdapter(applicationContext, arrayList)
                                        list_view_recycle.adapter = myAdapter
                                        list_view_recycle.layoutManager = LinearLayoutManager(
                                            applicationContext, LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        myAdapter.notifyDataSetChanged()

                                    }
                                }
                            }
                        }*//*
                        arrayList.add(productItem)
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/

     /*   //----------ORIGINAL------------
   //    var query : Query = myRef.child("Tops").orderByChild("product_name")
       var query : Query = myRef.orderByChild("product_name")

   //     var query : Query = myRef
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                    for (dss in snapshot.children) {
                            dss.children.forEach {

                            }
                            //utils.log("${dss.value}")

                            val productItem: Products? = dss.getValue(Products::class.java)
                            if (productItem != null) {


                                val myAdapter = MyStocksAdapter(applicationContext, arrayList)
                                list_view_recycle.adapter = myAdapter
                                list_view_recycle.layoutManager = LinearLayoutManager(
                                    applicationContext, LinearLayoutManager.VERTICAL,
                                    false
                                )
                                myAdapter.notifyDataSetChanged()

                                arrayList.add(productItem)
                            }


                    }

            }


            override fun onCancelled(error: DatabaseError) {
                utils.log("$error")
            }

        })
*/

//-----------MY STOCKS ADAPTER-----------------
       /* var loansRef: DatabaseReference = database.getReference("Loans")
        var productRef: DatabaseReference = database.getReference("Products")

        loansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                  //  val productItem : Products? = dss.getValue(Products::class.java)
                    dss.children.forEach {
                        val status = it.child("status").value
                        val sid=it.child("staffID").value
                        if (getStaffID().equals(sid)){
                            if (status.toString() == "pending") {
                                val loopName=it.child("productName")

                                loopName.children.forEach{
                                    val k=it.key
                                    val quantity=it.value


                                    textView_stock_name.text = k.toString()
                                    textView_stock_qty.text=quantity.toString()

                                    val myAdapter = MyStocksAdapter(applicationContext, arrayList)
                                    list_view_recycle.adapter = myAdapter
                                    list_view_recycle.layoutManager = LinearLayoutManager(
                                        applicationContext, LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    myAdapter.notifyDataSetChanged()

                                }

                            }
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
*/

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

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

                             if (staffID == dbSID) {
                                 if(status.toUpperCase() == "APPROVED"){
                                     val product = it.child("productName")
                                     product.children.forEach {
                                         val key = it.key.toString()

                                         val qty = it.value.toString().toInt()
                                         if (mutableList.containsKey(key)) {
                                             val oldValue = mutableList[key].toString().toInt()
                                             mutableList[key] = oldValue + qty

                                         } else {
                                             mutableList[key] = qty
                                         }

                                     }

                                 }
                             }

                         }
                     }

                     callback.push(mutableList)
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