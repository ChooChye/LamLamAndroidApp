package com.choochyemeilin.lamlam.Loans

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.choochyemeilin.lamlam.Loans.form.LoanAppForm
import com.choochyemeilin.lamlam.Loans.ui.main.SectionsPagerAdapter
import com.choochyemeilin.lamlam.R
import kotlinx.android.synthetic.main.activity_loans.*

class Loans : AppCompatActivity() {

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
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}