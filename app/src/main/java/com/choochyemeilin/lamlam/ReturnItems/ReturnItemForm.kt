package com.choochyemeilin.lamlam.ReturnItems

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.fromJson
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Retailers
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_stocks.*
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.return_item_form)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "Return Items Information"

        val data1 = intent.getStringExtra("data")
        var data: List<Products>? = null
        data = readJSON(data1!!)


        prodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = data!![0].product_name

                textView_product_name.text = "Pname: $name"

                for (dss in snapshot.children){
                    val qty = dss.child("qty").value
                    val pname=dss.child("product_name").value

                    if (pname==name){
                        textView_product_qty.text="$qty"
                    }
                }
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
                            val status=it.child("status").value
                            val product = it.child("productName")
                            val dbSID = it.child("staffID").value.toString()

                            Utils.getStaffID(object : FbCallback{
                                override fun onCallbackGetUserID(uid: Int) {
                                    super.onCallbackGetUserID(uid)

                                    var data2: List<Products>? = null
                                    data2 = readJSON(data1!!)
                                    val name = data2!![0].product_name

                                    if (uid.toString() == dbSID){

                                        //------------------------get all approved name qty??????????????????????---------------------------------
                                        if (status.toString().toUpperCase()=="APPROVED") {

                                            product.children.forEach {
                                                val key = it.key.toString()
                                                val qty = it.value.toString()

                                                textView_loan_name.text = "$key"
                                                textView_loan_qty.text  = qty

                                            }

                                        }
                                    }

                                }
                            })

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })




            button_return_now.setOnClickListener {

                if (return_qty.text.isEmpty()){
                    utils.toast(this,"Please enter quantity",1)
                }else  if (return_qty.text.isNotEmpty()){

                    if (return_qty.text.toString().toInt()>textView_loan_qty.text.toString().toInt()){
                      utils.toast(this,"Balance exceed",1)
                    }else if(return_qty.text.toString().toInt()>0){

                        val data = intent.getStringExtra("data")

                            prodRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var data2: List<Products>? = null
                                    data2 = readJSON(data1!!)
                                    val name = data2!![0].product_name

                                    for (dss in snapshot.children){
                                        val key=dss.key.toString()
                                        val value = dss.value.toString()
                                        val oldValue = dss.child("qty").value.toString().toInt()
                                        val enterValue=return_qty.text.toString().toInt()

                                        if (value.contains(name!!)) {
                                            val newValue = oldValue + enterValue
                                            prodRef.child(key).child("qty").setValue(newValue)

                                            //---------set time to show dialog-----then finish----------
                                        }
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })

                        loansRef.orderByKey()
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var data2: List<Products>? = null
                                    data2 = readJSON(data1!!)
                                    val name = data2!![0].product_name

                                    for (dss in snapshot.children) {
                                        val key1=dss.key.toString()

                                        dss.children.forEach {
                                            val key2=it.key.toString()
                                            val status=it.child("status").value
                                            val product = it.child("productName")
                                            product.children.forEach {
                                                val key = it.key.toString()
                                                val qty = it.value.toString()
                                                val lname=textView_loan_name.text
                                                val oldValue = qty.toInt()
                                                val enterValue=return_qty.text.toString().toInt()


                                                if (status.toString().toUpperCase()=="APPROVED"){
                                                    if (key.contains(name!!)){
                                                        val newValue = oldValue - enterValue
                                                        loansRef.child(key1).child(key2).child("productName").child(key).setValue(newValue)
                                                    }
                                                }

                                            }

                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })

                        updateDB(data.toString())

                }else if(return_qty.text.toString().toInt()<=0){
                        utils.toast(this,"Return quantity must be more than 0",1)
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
                val prodQty = return_qty.text.toString()  //NEED TO GET ReturnItemForm QUANTITY
                data[0].qty=return_qty.text.toString()
                val remark=return_remarks.text.toString()
                val status="IN STOCK"
                data[0].status="In Stock"
                val current = LocalDateTime.now()
                data[0].returnDate= current.toString()





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