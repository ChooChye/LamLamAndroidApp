package com.choochyemeilin.lamlam.Scan


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.Scan.ScanHistory.ScanHistory
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Products
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_scan.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val CAMERA_REQUEST_CODE = 101

class Scan : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private var utils: Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("ScanHistory")
    private var staffID = 0


    //Main Program
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Utils.getStaffID(object : FbCallback {
            override fun onCallbackGetUserID(uid: Int) {
                staffID = uid
                super.onCallbackGetUserID(uid)
            }
        })
        setupPermissions()
        codeScanner()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    hapticFeedback() //Vibrate the phone after successfully scanning
                    try {
                        val jsonData = it.toString()
                        var newJsonObj = JSONObject(jsonData)
                        var newJsonData = JSONArray()
                        newJsonObj.put("staffID", staffID)
                        newJsonData.put(newJsonObj)
                        codeScanner.stopPreview()

                        updateDB(newJsonData.toString())
                    } catch (e: Exception) {
                        showDialog("An error has occurred #9784 | ${e.message}")
                    }
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Utils.log("Camera Initialization error : ${it.message}")
                    Utils.toast(applicationContext, "ERROR QR", 1);
                }
            }
        }
        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    //Update Database after scanning
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDB(jsonData: String) {
        var data: List<Products>? = null
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //yyyy-MM-dd HH:mm:ss.SSS
        val formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss") //yyyy-MM-dd HH:mm:ss.SSS
        val formattedDate = current.format(formatter)
        val formattedTime = current.format(formatter2)

        try {
            data = readJSON(jsonData)
            //[{"category":"Tops","desc":"Tzu Pink Sweatshirt with Hoodie","image":"pink_sweatshirt.jpg","price":"39.00","product_name":"Tzu Pink Sweatshirt","qty":"80","id":"-MOyCeFDbzM2wbtV8Ied"}]
        } catch (e: Exception) {
            utils.log("Error #897 | $e")
        }
        val process =
            myRef.child(formattedDate).child(formattedTime).setValue(data)
        process
            .addOnSuccessListener {
                val cat = data!![0].category
                val id = data[0].id
                val prodPrice = data[0].price
                val prodName = data[0].product_name

                val msg = String.format(
                    "ID : %s\n" +
                            "Category : %s\n" +
                            "Product Name : %s\n" +
                            "Price : %s\n" +
                            "Successfully Recorded", id, cat, prodName, prodPrice
                )
                showDialog(msg)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.scan_history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.scanHistory_menu_btn -> startActivity(Intent(this, ScanHistory::class.java))
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}