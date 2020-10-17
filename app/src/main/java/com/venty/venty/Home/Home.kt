package com.venty.venty.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.venty.venty.R
import com.venty.venty.Scan.Scan

class Home : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<HomeItem> ? = null
    private var gridView: GridView? = null
    private var languageAdapter: HomeAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        //Init Var
        gridView = findViewById(R.id.homeGridLayout)
        arrayList = ArrayList()
        arrayList = setDataList()
        languageAdapter = HomeAdapter(applicationContext, arrayList!!)
        gridView?.adapter = languageAdapter
        gridView?.onItemClickListener = this
    }

    private fun setDataList() : ArrayList<HomeItem>{
        var arrayList:ArrayList<HomeItem> = ArrayList()

        arrayList.add(HomeItem(R.drawable.qr_code, "SCAN"))
        arrayList.add(HomeItem(R.drawable.magnifier, "SEARCH"))
        arrayList.add(HomeItem(R.drawable.loan, "LOANS"))
        arrayList.add(HomeItem(R.drawable.business_report , "REPORTS"))

        return arrayList
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                Toast.makeText(applicationContext, "SCAN", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Scan::class.java)
                startActivity(intent)
            }
            1 -> {
                Toast.makeText(applicationContext, "SEARCH", Toast.LENGTH_SHORT).show()
            }
            2 ->{
                Toast.makeText(applicationContext, "LOANS", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                Toast.makeText(applicationContext, "REPORTS", Toast.LENGTH_SHORT).show()
            }
        }
    }
}