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


    private fun nextPage(json: Any) {
        val intent = Intent(this, ReturnItemForm::class.java)
        intent.putExtra("data", json.toString())
        startActivity(intent)
        }


}