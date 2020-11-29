package com.choochyemeilin.lamlam.Home


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.Scan
import com.choochyemeilin.lamlam.helpers.Lcg
import com.choochyemeilin.lamlam.helpers.Utils
import kotlinx.android.synthetic.main.activity_home.*
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONObject

class Home : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var arrayList:ArrayList<HomeItem> ? = null
    private var gridView: GridView? = null
    private var languageAdapter: HomeAdapter? = null
    private var lcg : Lcg = Lcg()
    private var utils : Utils = Utils

    lateinit var toggle: ActionBarDrawerToggle
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        StrictMode.enableDefaults() //Enable thread policy to call internet service with one or more applications
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
                R.id.mItem2 -> Toast.makeText(
                    applicationContext,
                    "Clicked Item 2",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.mItem3 -> Toast.makeText(
                    applicationContext,
                    "Clicked Item 3",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        gridView = findViewById(R.id.homeGridLayout)
        arrayList = ArrayList()
        arrayList = setDataList()
        languageAdapter = HomeAdapter(applicationContext, arrayList!!)
        gridView?.adapter = languageAdapter
        gridView?.onItemClickListener = this

        httpRun()

        //val welcome = findViewById<TextView>(R.id.welcome_user)

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
        arrayList.add(HomeItem(R.drawable.business_report, "REPORTS"))
        arrayList.add(HomeItem(R.drawable.exchange, "RETURN ITEMS"))

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
            2 -> {
                Toast.makeText(applicationContext, "LOANS", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                Toast.makeText(applicationContext, "REPORTS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Navigation Drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun httpRun(){

        val url = "http://10.0.2.2/www/FYP/WebApp/android/connect.php" //change when going live

        var id : String ?= null
        var username : String ?= null

        try{
            val jsonObject: JSONObject = getJsonData("connect.php")
            id = jsonObject.getString("userID")
            username = jsonObject.getString("userName")

            utils.log("Test = passed. Connected to MySQL | ${id}  ${username}")
        }catch (e: Exception){
            utils.log("An error has occurred Error #1654")
        }
    }

    private fun getJsonData(phpFile : String) : JSONObject{
        val url = "http://10.0.2.2/www/FYP/WebApp/android/$phpFile"
        var jsonObject: JSONObject ?=null
        try{
            val httpClient : HttpClient = DefaultHttpClient()
            val httpPost : HttpPost = HttpPost(url)
            val httpResp : HttpResponse = httpClient.execute(httpPost)
            val respBody = EntityUtils.toString(httpResp.entity)

            val parseJson : JSONArray = JSONArray(respBody)

            for (i in 0 until parseJson.length()) {
                jsonObject = parseJson.getJSONObject(i)
            }
        }catch (e: Exception){
            utils.log("error getting json Error #1532")
        }
        return jsonObject!!
    }
}