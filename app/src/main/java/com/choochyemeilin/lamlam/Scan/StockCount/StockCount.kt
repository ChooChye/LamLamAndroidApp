package com.choochyemeilin.lamlam.Scan.StockCount

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.ScanHistory.ScanHistoryObj
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_stock_count.*


class StockCount : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myLoans: DatabaseReference = database.getReference("Loans")
    private var myScanHistory: DatabaseReference = database.getReference("ScanHistory")
    private lateinit var viewAdapter: StockCountAdapter
    private var staffID = 0

    private var dataLoans: MutableMap<String, Int> = mutableMapOf()
    private var dataScanHistory: MutableMap<String, Int> = mutableMapOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_count)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Stocks Counted")
        supportActionBar?.elevation = 0f


        Utils.getStaffID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                staffID = uid
                super.onCallbackGetUserID(uid)
            }
        })
        getLoans(object : FbCallback {
            override fun push(map: MutableMap<String, Int>) {
                //Utils.log("MAP  = ${map.size}")
                dataLoans = map
                super.push(map)
            }
        })

        getScanHistory(object : FbCallback {
            override fun push(arr: MutableMap<String, Int>) {
                //Utils.log("Arr = ${arr.size}")
                dataScanHistory = arr
                super.push(arr)
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            dataLoans.clear()
            dataScanHistory.clear()

            if(dataLoans.isEmpty()){
                stock_count_tv_result.visibility = View.VISIBLE
                stock_count_rv.visibility = View.GONE
                stockCount_progressBar.visibility = View.GONE
            }else{
                stock_count_rv.apply {
                    adapter = StockCountAdapter(dataLoans, dataScanHistory)
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                }
                stockCount_progressBar.visibility = View.GONE
            }
        }, 2000)

    }

    private fun getLoans(fbCallback: FbCallback) {
        val mutableMap: MutableMap<String, Int> = mutableMapOf()


        //Get Loans
        myLoans.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { jt ->
                    jt.children.forEach { lt ->
                        val dbStaff = lt.child("staffID").value.toString().toInt()
                        val status = lt.child("status").value.toString()
                        if (dbStaff == staffID) {
                            if (status == "approved") {
                                val prodName = lt.child("productName")
                                prodName.children.forEach {
                                    val key = it.key.toString()
                                    val qty = it.value.toString().toInt()
                                    if (mutableMap.containsKey(key)) {
                                        val oldValue = mutableMap[key].toString().toInt()
                                        mutableMap[key] = oldValue + qty
                                    } else {
                                        mutableMap[key] = qty
                                    }
                                }
                            }
                        }

                    }
                }
                fbCallback.push(mutableMap)
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })
    }

    private fun getScanHistory(fbCallback: FbCallback) {
        //Get ScanHistory
        val arrScanHistory: ArrayList<ScanHistoryObj> = arrayListOf()
        val mutableMapScan : MutableMap<String, Int> = mutableMapOf()
        myScanHistory.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    snapshot.children.forEach {
                        it.children.forEach { jt ->
                            jt.children.forEach { kt ->
                                val dbStaff = kt.child("staffID").value.toString().toInt()
                                if (dbStaff == staffID) {
                                    val prodName = kt.child("product_name").value.toString()
                                    val scannedQty = kt.child("scannedQty").value.toString().toInt()

                                    if (mutableMapScan.containsKey(prodName)) {
                                        val oldValue = mutableMapScan[prodName].toString().toInt()
                                        mutableMapScan[prodName] = oldValue + scannedQty
                                    } else {
                                        mutableMapScan[prodName] = scannedQty
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Utils.log(e.message.toString())
                }
                fbCallback.push(mutableMapScan)
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}