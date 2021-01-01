package com.choochyemeilin.lamlam.Loans

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import androidx.viewpager.widget.ViewPager
import com.choochyemeilin.lamlam.Home.Home
import com.choochyemeilin.lamlam.Loans.form.LoanAppForm
import com.choochyemeilin.lamlam.Loans.ui.main.SectionsPagerAdapter
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class Loans : AppCompatActivity() {

    private var utils : Utils = Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loans)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        val fab: FloatingActionButton = findViewById(R.id.fab)


        //Init Fragments
        sectionsPagerAdapter.addFragment(LoansPending(), "Pending Loans")
        sectionsPagerAdapter.addFragment(MyLoans(), "My Loans")
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)


        setSupportActionBar(findViewById(R.id.loans_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("LOANS")

        fab.setOnClickListener {
            startActivity(Intent(this, LoanAppForm::class.java))
            this.finish()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, Home::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        this.finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}