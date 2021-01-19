package com.choochyemeilin.lamlam.ReturnItems

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
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
    private var prodRef: DatabaseReference = database.getReference("Products")
    private var loansRef: DatabaseReference = database.getReference("Loans")

    private lateinit var auth: FirebaseAuth
    private var test1 = 10
    private var test2  = 5

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
        val data1 = intent.getStringExtra("data")
        var data: List<Products>? = null
        data = readJSON(data1!!)
        prodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = data!![0].product_name
                val quantity = data!![0].qty
                textView_product_name.text = "Product Quantity : $name"
                textView_product_qty.text = "$quantity"
                test1=quantity.toInt()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        loansRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        dss.children.forEach {

                            val product = it.child("productName")
                            product.children.forEach {
                                val key = it.key.toString()
                                val qty = it.value.toString()
                               textView_loan_name.text="Loan Name : $key"
                                textView_loan_qty.text= "$qty"
                               test2=qty.toInt()

                           //     val test=test1+test2
                          //      textView_return_qty.text= test.toString()
                            }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

   /*  // -------------------------------------------------------------
        val pqty=  textView_product_qty.text as Int
        val lqty= textView_loan_qty.text as Int
        textView_return_qty.text= pqty.minus(lqty).toString()*/





        button_return_now.setOnClickListener {
            if(return_qty.text.isEmpty()){
                utils.toast(this,"Please enter quantity",1)
            }else if(return_qty.text.toString().toInt()>textView_loan_qty.text.toString().toInt()){
                  utils.toast(this,"Balance exceed",1)

            }else{
                val data = intent.getStringExtra("data")

                if (data != null) {
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
                val prodName = data[0].product_name
              //  val prodQty = return_qty.text.toString()  //NEED TO GET ReturnItemForm QUANTITY
                data[0].qty=return_qty.text.toString()
                val remark=return_remarks.text.toString()
                val status="IN STOCK"
                data[0].status="In Stock"
                val current = LocalDateTime.now()
                data[0].returnDate= current.toString()

                val prodQty = textView_product_qty.text.toString().toInt()+return_qty.text.toString().toInt()

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
                        prodQty.toString(),
                        image,
                        status,
                        loanDate,
                        current.toString(),
                        remark
                    ))

              /* //---------------------------------------------------------------------------------------------------
              prodRef.orderByChild("qty").addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       var test=snapshot.value
                        test==prodQty
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })*/

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