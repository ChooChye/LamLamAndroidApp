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

       /* val data = intent.getStringExtra("data")
        try {
            var data1: List<Products>? = null
            val json = data
             data1=readJSON(json!!)

            Utils.log(data1.toString())
            Utils.log(data1[0].product_name.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }*/


        button_return_now.setOnClickListener {
            if(return_qty.text.isEmpty()){
                utils.toast(this,"Please enter quantity",1)
            }else{

                //!!!!!!!!!!!!!!PENDING PENDING PENDING!!!!!!!!!!!!!!!!!!!!!!!

                val data = intent.getStringExtra("data")
                //androidx.appcompat.widget.AppCompatButton{d82bddb VFED..C.. ...P.... 330,413-750,545 #7f080066 app:id/button_return_now}

                if (data != null) { //[{"category":"Tops","desc":"Tzu Pink Sweatshirt with Hoodie","image":"pink_sweatshirt.jpg","price":"39.00","product_name":"Tzu Pink Sweatshirt","qty":"1","id":"-MOyCeFDbzM2wbtV8Ied"}]
                    updateDB(data)
                }


            }

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
                val prodQty = return_qty.text.toString()  //NEED TO GET ReturnItemForm QUANTITY
                data[0].qty=return_qty.text.toString()
                val remark=return_remarks.text.toString()
                //  val oldStatus = data[0].status
                val status="IN STOCK"
              //  oldStatus.equals(status)
                data[0].status="In Stock"
                //     val returnDate = data[0].returnDate
                val current = LocalDateTime.now()
                data[0].returnDate= current.toString()
                //     val remarks=  // NEED TO GET ReturnItemForm REMARKS

                val msg = String.format(
                    "Product ID : %s\n" +
                            "Category : %s\n" +
                            "Product Name : %s\n" +
                            "Quantity : %s\n" +
                            "Status : %s\n" +
                            "Return Date : %s\n\n" +
                            "Remarks : %s\n\n" +
                            "Successfully Recorded", id, cat, prodName, prodQty, status, current,remark
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