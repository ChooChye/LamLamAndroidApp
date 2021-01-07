package com.choochyemeilin.lamlam.Misc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choochyemeilin.lamlam.R
import kotlinx.android.synthetic.main.activity_no_internet.*

class NoInternet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
        supportActionBar!!.hide()

        nointernet_btn_ok.setOnClickListener {
            finish()
            System.exit(-1)
        }
    }
}