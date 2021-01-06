package com.choochyemeilin.lamlam.Home


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.Loans.Loans
import com.choochyemeilin.lamlam.Login.Login
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Reports.Reports
import com.choochyemeilin.lamlam.ReturnItems.MyStocks
import com.choochyemeilin.lamlam.ReturnItems.ReturnItems
import com.choochyemeilin.lamlam.Scan.Scan
import com.choochyemeilin.lamlam.Search.Search
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Lcg
import com.choochyemeilin.lamlam.helpers.Retailers
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

import org.json.JSONArray
import org.json.JSONObject

class Home : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<HomeItem> ? = null
    private var gridView: GridView? = null
    private var languageAdapter: HomeAdapter? = null
    //private var lcg : Lcg = Lcg()

    lateinit var toggle: ActionBarDrawerToggle
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Init Var

        //Navigation Bar
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)
        supportActionBar?.elevation = 0f

        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mItem1 -> Toast.makeText(
                    applicationContext,
                    "Clicked Item 1",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.mItem2 -> {
                    startActivity(Intent(this, MyStocks::class.java))
                    finish()
                }

                R.id.mItem3 -> logout()

            }
            true
        }

        gridView = findViewById(R.id.homeGridLayout)
        arrayList = ArrayList()
        arrayList = setDataList()
        languageAdapter = HomeAdapter(applicationContext, arrayList!!)
        gridView?.adapter = languageAdapter
        gridView?.onItemClickListener = this


    }

    //Logout Methods
    private fun logout(){

        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun setDataList() : ArrayList<HomeItem>{
        val arrayList:ArrayList<HomeItem> = ArrayList()

        arrayList.add(HomeItem(R.drawable.qr_code, "SCAN"))
        arrayList.add(HomeItem(R.drawable.magnifier, "SEARCH"))
        arrayList.add(HomeItem(R.drawable.loan, "LOANS"))
        arrayList.add(HomeItem(R.drawable.business_report, "REPORTS"))
        arrayList.add(HomeItem(R.drawable.exchange, "RETURN ITEMS"))

        return arrayList
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> startActivity(Intent(this, Scan::class.java))
            1 -> startActivity(Intent(this, Search::class.java))
            2 -> startActivity(Intent(this, Loans::class.java))
            3 -> startActivity(Intent(this, Reports::class.java))
            4 -> startActivity(Intent(this, ReturnItems::class.java))
        }
    }

    //Navigation Drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}