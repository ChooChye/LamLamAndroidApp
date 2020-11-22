package com.venty.venty.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.venty.venty.R
import com.venty.venty.Scan.Scan
import com.venty.venty.helpers.Lcg
import kotlinx.android.synthetic.main.activity_home.*
import org.w3c.dom.Text
import java.lang.reflect.Array
import kotlin.experimental.xor

class Home : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<HomeItem> ? = null
    private var gridView: GridView? = null
    private var languageAdapter: HomeAdapter? = null
    private var lcg : Lcg = Lcg()
    
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

        val welcome = findViewById<TextView>(R.id.welcome_user)

        /*val str : String = "T"

        var chars = str.toCharArray()
        var i = 0;
        *//*var n = mutableListOf<Int>();
        while(i < 8){
            n.add(i, chars[i].toInt() xor lcg.next().toInt())
            i++
        }*//*
        val binary = chars[0].toInt()
        val finalBinary = String.format("%8s", Integer.toBinaryString(binary)).replace(' ', '0')
        welcome.text = lcg.toBinary(chars).toString()*/
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