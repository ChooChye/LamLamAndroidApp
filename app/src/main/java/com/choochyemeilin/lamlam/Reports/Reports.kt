package com.choochyemeilin.lamlam.Reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choochyemeilin.lamlam.R

class Reports : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("REPORTS")
        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}