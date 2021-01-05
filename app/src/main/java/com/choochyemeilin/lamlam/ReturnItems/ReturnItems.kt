package com.choochyemeilin.lamlam.ReturnItems

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.fromJson
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_return_items.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val CAMERA_REQUEST_CODE = 101

class ReturnItems : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private var utils: Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("Return History")
    private var productRef: DatabaseReference = database.getReference("Products")

    //Main Program
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_items)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = "Return Items"
        setupPermissions()
        codeScanner()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun codeScanner() {
        codeScanner = CodeScanner(this, return_scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    hapticFeedback()
                    try {
                        val jsonData = "[$it]"
                        utils.log(it.toString())
                        //[{ "id":"-MOMC5KxRtiN1NIlAPZC", "category":"Tops", "product":[{ "desc":"Pink Sweatshirt with Logo", "price":"39.00", "product_name":"Pink Sweatshirt", "qty":"1" }] }]
                        codeScanner.stopPreview()

                        nextPage(jsonData)




                    //    updateDB(jsonData)
                    } catch (e: Exception) {
                        utils.log(e.toString())
                        showDialog("An error has occurred #9784 | $e")
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera Initialization error : ${it.message}")
                }
            }
        }

        return_scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
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
                showDialog(msg)

                Toast.makeText(this, "Items Return Successfully", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener {
                showDialog("Firebase error")
            }

    }

    //Vibrate when scanning
    private fun hapticFeedback() {
        val time: Long = 200
        val v = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    time,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            v.vibrate(time)
        }
    }

    private fun readJSON(json: String): List<Products> {
        return if (json != null)
            Gson().fromJson(json) //GsonExtension Call
        else
            listOf()
    }

    //Check if camera permissions is granted
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    //Check if camera permissions is granted else make request
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to be able to use this app!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //success
                }
            }
        }
    }

    //Show Dialog
    private fun showDialog(msg: String) {
        var builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder
            .setTitle("INFORMATION")
            .setMessage(msg)
            .setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                codeScanner.startPreview()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    fun updateStatus(){
        var query : Query = productRef.orderByChild("status")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    // arrayList.clear()
                    for (dss in snapshot.children) {
                        //utils.log("${dss.value}")
                        val productStatus: Products? = dss.getValue(Products::class.java)


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                utils.log("$error")
            }

        })
    }

    private fun nextPage(json: Any) {

          //  val b = Bundle()
          //  b.putStringArrayList("arrayListText", arrayListText)
       // b.putParcelableArrayList("arrayListText", data)
        //    val intent = Intent(this, ReturnItemForm::class.java)
           // intent.putExtras(b)
      //      startActivity(intent)

        val intent = Intent(this, ReturnItemForm::class.java)
        intent.putExtra("data", json.toString())
        startActivity(intent)
        }


}