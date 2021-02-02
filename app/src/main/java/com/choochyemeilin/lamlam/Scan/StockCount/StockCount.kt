package com.choochyemeilin.lamlam.Scan.StockCount

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.ScanHistory.ScanHistoryObj
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_stock_count.*
import java.lang.reflect.Type

class StockCount : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myLoans: DatabaseReference = database.getReference("Loans")
    private var myScanHistory: DatabaseReference = database.getReference("ScanHistory")
    private lateinit var viewAdapter: StockCountAdapter
    private var staffID = 0

    private var dataMap: MutableMap<String, Int> = mutableMapOf()
    private var dataArr: ArrayList<ScanHistoryObj> = arrayListOf()

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

        /*getData(object : FbCallback {
            override fun stockCountCallback(
                map: MutableMap<String, Int>,
                arr: ArrayList<ScanHistoryObj>
            ) {
                Utils.log("MAP  = ${map.size} | Arr = ${arr.size}")
                dataMap = map
                super.push(map)
            }
        })*/

        getLoans(object : FbCallback {
            override fun push(map: MutableMap<String, Int>) {
                Utils.log("MAP  = ${map.size}")
                dataMap = map
                super.push(map)
            }
        })

        getScanHistory(object : FbCallback{
            override fun scanHistoryArrCallback(arr: ArrayList<ScanHistoryObj>) {
                Utils.log("Arr = ${arr.size}")
                super.scanHistoryArrCallback(arr)
            }
        })


        /*if (dataMap.isEmpty()) {
            stock_count_tv_result.visibility = View.VISIBLE
            stock_count_rv.visibility = View.GONE
        } else {
            stock_count_tv_result.visibility = View.GONE
            viewAdapter = StockCountAdapter(dataMap, dataArr)
            stock_count_rv.apply {
                adapter = viewAdapter
                layoutManager = LinearLayoutManager(applicationContext)
                addItemDecoration(
                    DividerItemDecoration(
                        applicationContext,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }*/

    }

    private fun getData(fbCallback: FbCallback) {
        val mutableMap: MutableMap<String, Int> = mutableMapOf()
        val arrScanHistory: ArrayList<ScanHistoryObj> = arrayListOf()

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
            }//END of myLoans

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })
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
        myScanHistory.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    snapshot.children.forEach {
                        it.children.forEach { jt ->
                            jt.children.forEach { kt ->
                                val dbStaff = kt.child("staffID").value.toString().toInt()
                                if (dbStaff == staffID) {
                                    val obj: ScanHistoryObj =
                                        kt.getValue(ScanHistoryObj::class.java)!!
                                    arrScanHistory.add(obj)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Utils.log(e.message.toString())
                }
                fbCallback.scanHistoryArrCallback(arrScanHistory)
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