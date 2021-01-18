package com.choochyemeilin.lamlam.ReturnItems

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.fromJson
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Retailers
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
    private var newRef: DatabaseReference = database.getReference("All Stocks")
    private var mutableList: MutableMap<String, Int> = mutableMapOf()
    private lateinit var auth: FirebaseAuth
    private var staffID = 0
    private var retailerID  = 0

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
                val desc=data[0].desc
                val price=data[0].price
                val image=data[0].image
                val loanDate=data[0].loanDate
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
                showDialog(msg)

                myRef.child(formattedDate).child(formattedTime).child(formattedSec)
                    .setValue(Products(
                        id,
                        cat,
                        prodName,
                        desc,
                        price,
                        prodQty,
                        image,
                        status,
                        loanDate,
                        current.toString(),
                        remark
                    ))

               /* Utils.getRetailerID(object : FbCallback{
                    override fun onCallbackGetRetailerID(rid: Int) {
                        super.onCallbackGetRetailerID(rid)
                        retailerID = rid


                    }
                })*/

               /* Utils.getStaffID(object : FbCallback{
                    override fun onCallbackGetUserID(uid: Int) {
                        super.onCallbackGetUserID(uid)
                        staffID = uid
                        newRef.child(retailerID.toString()).child("staffID").setValue(uid.toString())
                    }
                })*/




               /* newRef
                    .child(Utils.getRetailerID(object : FbCallback{
                        override fun onCallbackGetRetailerID(rid: Int) {
                            super.onCallbackGetRetailerID(rid)
                            retailerID = rid
                        }
                    }).toString())
                    .child(          Utils.getStaffID(object : FbCallback{
                        override fun onCallbackGetUserID(uid: Int) {
                            super.onCallbackGetUserID(uid)
                            staffID = uid
                        }
                    }).toString())
                    .child("prodName")
                    .setValue(prodName,prodQty)*/

                Toast.makeText(this, "Items Return Successfully", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener {
                showDialog("Firebase error")
            }

    }


    private fun showDialog(msg: String) {
        var builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder
            .setTitle("INFORMATION")
            .setMessage(msg)
            .setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}