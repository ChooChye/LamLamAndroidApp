package com.choochyemeilin.lamlam.Reports

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.activity_reports.*
import java.text.SimpleDateFormat
import java.util.*

class Reports : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private var year  : Int = 0
    private var month : Int  = 0
    private var day   : Int  = 0

    private var savedYear  : Int = 0
    private var savedMonth : Int  = 0
    private var savedDay   : Int  = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("REPORTS")
        supportActionBar?.elevation = 0f

        pickStartDate()
        pickEndDate()
    }

    private fun getDateCalendar(){
        val cal : Calendar = Calendar.getInstance()

        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickStartDate(){
        reports_tv_StartDate.setOnClickListener{
            getDateCalendar()
            val dialog = DatePickerDialog(this, R.style.DatePickerTheme, this, year, month, day)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
    private fun pickEndDate(){
        reports_tv_endDate.setOnClickListener{
            getDateCalendar()
            val dialog = DatePickerDialog(this, R.style.DatePickerTheme, this, year, month, day)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = p3
        savedMonth = p2
        savedYear = p1


        getDateCalendar()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val formatted = sdf.format(Date(savedYear-1900,savedMonth,savedDay))
        reports_tv_StartDate.text = formatted
        Utils.log("${p0?.id}")
    }
}