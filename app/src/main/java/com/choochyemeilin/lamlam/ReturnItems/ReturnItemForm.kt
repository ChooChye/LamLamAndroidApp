package com.choochyemeilin.lamlam.ReturnItems

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.fromJson
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.return_item_form.*
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReturnItemForm : AppCompatActivity() {
    private var utils: Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Return History")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.return_item_form)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "Return Items Information"

        val data = intent.getStringExtra("data")
        try {
            var data1: List<Products>? = null
            val json = data
             data1=readJSON(json!!)

            Utils.log(data1.toString())
            Utils.log(data1[0].product_name.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        button_return_now.setOnClickListener {
            val jsonData = "[$it]"
            updateDB(jsonData)
        }
    }

    private fun readJSON(json: String): List<Products> {

        return if (json != null)
            Gson().fromJson(json) //GsonExtension Call
        else
            listOf()
    }

    //Update Database after scanning
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDB(jsonData: String) {
        var data: List<Products>? = null
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //yyyy-MM-dd HH:mm:ss.SSS
        val formatter2 = DateTimeFormatter.ofPattern("HH:mm") //yyyy-MM-dd HH:mm:ss.SSS
        val formatter3 = DateTimeFormatter.ofPattern("ss") //yyyy-MM-dd HH:mm:ss.SSS
        val formattedDate = current.format(formatter)
        val formattedTime = current.format(formatter2)
        val formattedSec = current.format(formatter3)

        try {
            data = readJSON(jsonData)

        } catch (e: Exception) {
            utils.log("Error #897 | $e")
        }
        //utils.log("TEST readJSON = $data");

        val process =
            myRef.child(formattedDate).child(formattedTime).child(formattedSec).setValue(data)
        process
            .addOnSuccessListener {
                val cat = data!![0].category
                val id = data[0].id

                //val prodDesc = data!![0].desc
                val prodName = data[0].product_name
                val prodQty = data[0].qty  //NEED TO GET ReturnItemForm QUANTITY

                val oldStatus = data[0].status
                val status="IN STOCK"
                oldStatus.equals(status)
                //     val returnDate = data[0].returnDate
                val current = LocalDateTime.now()
                //     val remarks=  // NEED TO GET ReturnItemForm REMARKS

                val msg = String.format(
                    "Product ID : %s\n" +
                            "Category : %s\n" +
                            "Product Name : %s\n" +
                            "Quantity : %s\n" +
                            "Status : %s\n" +
                            "Return Date : %s\n\n" +
                            "Remarks : \n\n" +
                            "Successfully Recorded", id, cat, prodName, prodQty, status, current
                )
              //  showDialog(msg)

                Toast.makeText(this, "Items Return Successfully", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener {
           //     showDialog("Firebase error")
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}