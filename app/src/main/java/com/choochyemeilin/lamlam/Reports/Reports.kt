package com.choochyemeilin.lamlam.Reports

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.choochyemeilin.lamlam.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.Reports.adapters.ReportAdapter
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_reports.*
import okhttp3.internal.notifyAll
import java.text.SimpleDateFormat
import java.util.*


class Reports : AppCompatActivity() {

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private var savedYear: Int = 0
    private var savedMonth: Int = 0
    private var savedDay: Int = 0

    private var cal: Calendar = Calendar.getInstance()
    private lateinit var mStartDateListenerStart: OnDateSetListener
    private lateinit var mEndDateListenerStart: OnDateSetListener

    private var mutableList: MutableMap<String, Int> = mutableMapOf()
    private var rList: MutableMap<String, Int> = mutableMapOf()
    private var retailerID : Int? = 0

    private var sortByQty = 0 // 0 = sort by Highest, 1 = sort by Lowest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("REPORTS")
        supportActionBar?.elevation = 0f

        Utils.getRetailerID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                super.onCallbackGetUserID(uid)
                retailerID = uid
            }
        })

        pickStartDate()
        pickEndDate()
        reports_btn_searchBtn.setOnClickListener {
            val startDate = reports_tv_StartDate.text.toString()
            val endDate = reports_tv_endDate.text.toString()

            if(startDate.isNotEmpty() && endDate.isNotEmpty()){
                getData(object : FbCallback {
                    override fun push(arr: MutableMap<String, Int>) {
                        super.push(arr)
                        rList = arr
                        reports_rv.adapter = ReportAdapter(arr)
                        reports_rv.layoutManager = LinearLayoutManager(applicationContext)
                        reports_rv.setHasFixedSize(true)
                        if (arr.isEmpty()) {
                            reports_tv_message.text = "No results found"
                        } else {
                            reports_tv_message.visibility = View.GONE
                        }
                    }
                })
            }else{
                Utils.toast(applicationContext, "Please select a Start & End date", 1)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.report_filters, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.reports_menu_sort -> sortDataByQty()
            R.id.reports_menu_sortAlpha -> sortDataByAlpha()
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    private fun sortDataByQty(){
        var map : MutableMap<String, Int>
        if(sortByQty == 0){
            sortByQty = 1
            val sorted = mutableList.toList().sortedBy { col ->col.second}.toMap()
            map = sorted.toMutableMap()
        }else{
            sortByQty = 0
            val sorted = mutableList.toList().sortedByDescending { col ->col.second}.toMap()
            map = sorted.toMutableMap()

        }
        reports_rv.adapter = ReportAdapter(map)
        reports_rv.layoutManager = LinearLayoutManager(applicationContext)
        reports_rv.setHasFixedSize(true)
    }

    private fun sortDataByAlpha(){
        var map : MutableMap<String, Int>
        if(sortByQty == 0){
            sortByQty = 1
            val sorted = mutableList.toList().sortedBy { col ->col.first}.toMap()
            map = sorted.toMutableMap()
        }else{
            sortByQty = 0
            val sorted = mutableList.toList().sortedByDescending { col ->col.first}.toMap()
            map = sorted.toMutableMap()
        }
        reports_rv.adapter = ReportAdapter(map)
        reports_rv.layoutManager = LinearLayoutManager(applicationContext)
        reports_rv.setHasFixedSize(true)
    }

    private fun getData(callback: FbCallback) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Loans")

        val startDate = reports_tv_StartDate.text.toString()
        val endDate = reports_tv_endDate.text.toString()


        myRef.orderByKey().startAt(startDate).endAt(endDate)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mutableList.clear()
                    for (dss in snapshot.children) {
                        dss.children.forEach {
                            val dbRID = it.child("retailerID").value.toString().toInt()
                            val status = it.child("status").value.toString()
                            if (retailerID == dbRID) {
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

    private fun getDateCalendar() {
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickStartDate() {
        var date: String = ""
        reports_tv_StartDate.setOnClickListener {
            getDateCalendar()
            mStartDateListenerStart =
                OnDateSetListener { p0, p1, p2, p3 ->
                    date = initDate(p1, p2, p3)
                    reports_tv_StartDate.text = date
                }
            val dialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                mStartDateListenerStart,
                year,
                month,
                day
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setButton(
                DialogInterface.BUTTON_POSITIVE,
                "OK"
            ) { p0, p1 ->
                if (p1 == DialogInterface.BUTTON_POSITIVE) {
                    //Set selected date to end datepicker
                    val datepicker = dialog.datePicker
                    mStartDateListenerStart.onDateSet(
                        datepicker,
                        datepicker.year,
                        datepicker.month,
                        datepicker.dayOfMonth
                    )
                }
            }
            dialog.show()
        }
    }

    private fun initDate(p1: Int, p2: Int, p3: Int): String {
        savedDay = p3
        savedMonth = p2
        savedYear = p1
        //Default Format January 1, 1970 00:00:00
        getDateCalendar()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date(savedYear - 1900, savedMonth, savedDay))
    }

    private fun pickEndDate() {
        reports_tv_endDate.setOnClickListener {
            mEndDateListenerStart =
                OnDateSetListener { p0, p1, p2, p3 ->
                    reports_tv_endDate.text = initDate(p1, p2, p3)
                }
            val dialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                mEndDateListenerStart,
                year,
                month,
                day
            )
            val startDate = reports_tv_StartDate.text.toString()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            formatter.isLenient = false
            val toMil = formatter.parse(startDate).time
            dialog.datePicker.minDate = toMil

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
}