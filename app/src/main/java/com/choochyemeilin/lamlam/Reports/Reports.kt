package com.choochyemeilin.lamlam.Reports

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Loans.Classes.SelectedProducts
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Reports.objects.ReportsObject
import kotlinx.android.synthetic.main.activity_reports.*
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

    private var arrayList : ArrayList<ReportsObject> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("REPORTS")
        supportActionBar?.elevation = 0f

        pickStartDate()
        pickEndDate()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}