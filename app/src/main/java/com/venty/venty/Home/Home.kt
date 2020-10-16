package com.venty.venty.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.venty.venty.R

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
                Toast.makeText(applicationContext, "Scan", Toast.LENGTH_SHORT)
//                var securityIntent = Intent(this, Security::class.java)
//                startActivity(securityIntent)
            }
            1 -> {
//                var smartLightsIntent = Intent(this, SmartLights::class.java)
//                startActivity(smartLightsIntent)
            }
            2 ->{
//                var ThemIntent = Intent(this, Thermometer::class.java)
//                startActivity(ThemIntent)
            }
            3 -> {
//                var ultrasonicIntent = Intent(this, Ultrasonic::class.java)
//                startActivity(ultrasonicIntent)
            }
        }
    }
}