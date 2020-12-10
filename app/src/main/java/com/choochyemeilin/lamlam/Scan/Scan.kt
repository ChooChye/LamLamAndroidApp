package com.choochyemeilin.lamlam.Scan


import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_scan.*
import java.lang.Exception
import java.lang.reflect.Executable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val CAMERA_REQUEST_CODE = 101

class Scan : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    //private var utils: Utils = Utils
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = database.getReference("ScanHistory")


    //Main Program
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
                    hapticFeedback()
                    try {
                        val jsonData = "[$it]" //{"id":"5fcb238224456","Category":"Tops","Product":"Pink Sweatshirt"}
                        codeScanner.stopPreview()
                        updateDB(jsonData)
                    }catch (e : Exception){
                        showDialog("An error has occurred #9784")
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera Initialization error : ${it.message}")
                }
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    //Update Database after scanning
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDB(jsonData: String): Boolean {
        var stat = true
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //yyyy-MM-dd HH:mm:ss.SSS
        val formatter2 = DateTimeFormatter.ofPattern("HH:mm") //yyyy-MM-dd HH:mm:ss.SSS
        val formatter3 = DateTimeFormatter.ofPattern("ss") //yyyy-MM-dd HH:mm:ss.SSS
        val formattedDate = current.format(formatter)
        val formattedTime = current.format(formatter2)
        val formattedSec = current.format(formatter3)

        val data = readJSON(jsonData)

        val process = myRef.child(formattedDate).child(formattedTime).child(formattedSec).setValue(data)
        process
            .addOnSuccessListener {
                val cat = data[0].getCategory()
                val prod = data[0].getProduct()
                val id = data[0].getID()

                val msg =   "ID : $id \n" +
                            "Category : $cat \n" +
                            "Product : $prod \n" +
                        "Successfully recorded"

                showDialog(msg)

            }
            .addOnFailureListener {
                showDialog("Firebase error")
            }
        return stat
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

    private fun readJSON(json: String): List<ScanHistory> {
        return if (json != null)
            Gson().fromJson(json) //Extension Call
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
}