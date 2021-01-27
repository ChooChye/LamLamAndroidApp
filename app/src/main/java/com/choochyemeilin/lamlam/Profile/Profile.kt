package com.choochyemeilin.lamlam.Profile

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Retailers
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.profile_change_password_dialog.*
import kotlinx.android.synthetic.main.profile_change_password_dialog.view.*
import com.choochyemeilin.lamlam.R
import com.google.android.material.snackbar.Snackbar


class Profile : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = database.getReference("User")

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Profile")
        supportActionBar?.elevation = 0f

        Utils.getRetailerInfo(object : FbCallback {
            override fun onCallbackRetailer(arr: ArrayList<Retailers>) {
                profile_tv_userAddress.text = arr[0].rAddress
            }
        })

        loadUser(object : FbCallback {
            override fun profileCallback(arr: MutableMap<String, String>) {
                val x = arr.toList()
                profile_tv_userName.text = x[0].first
                profile_tv_userEmail.text = x[0].second
            }
        })

        profile_cv_changePassword.setOnClickListener { showPasswordDialog() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    private fun loadUser(fbCallback: FbCallback) {
        val user = auth.currentUser?.email

        val mutableList: MutableMap<String, String> = mutableMapOf()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    val staffEmail = dss.child("staffEmail").value.toString()
                    if (user == staffEmail) {
                        val userName = dss.child("staffName").value.toString()
                        val userEmail = dss.child("staffEmail").value.toString()
                        mutableList[userName] = userEmail
                    }
                }
                fbCallback.profileCallback(mutableList)
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.log("Error has occurred #9763 | ${error.message}")
            }
        })
    }

    //Show Dialog
    private fun showPasswordDialog() {
        val view: View = View.inflate(this, R.layout.profile_change_password_dialog, null)
        val builder: AlertDialog = AlertDialog.Builder(this)
            .setTitle("CHANGE PASSWORD")
            .setView(view)
            .setPositiveButton("UPDATE", null)
            .setNeutralButton("CANCEL", null)
            .create()

        builder.setOnShowListener {
            val button: Button = builder.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val cPass = view.profile_et_currentPassword
                val nPass = view.profile_et_newPassword

                if (TextUtils.isEmpty(cPass.text.toString())) {
                    cPass.setError("Please enter your current password")
                    cPass.requestFocus()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(nPass.text.toString())) {
                    nPass.setError("Please enter your new password")
                    nPass.requestFocus()
                    return@setOnClickListener
                }else if(nPass.length() < 6) {
                    nPass.setError("Password must be at least 6 characters")
                    nPass.requestFocus()
                    return@setOnClickListener
                }else{
                    updatePassword(cPass.text.toString(), nPass.text.toString(), view)
                    builder.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun updatePassword(cPass: String, nPass: String, v: View) {
        val user = auth.currentUser
        val credential: AuthCredential = EmailAuthProvider.getCredential(user!!.email!!, cPass)

        user.reauthenticate(credential).addOnCompleteListener { jt ->
            if (jt.isSuccessful) {
                user.updatePassword(nPass).addOnCompleteListener {
                    if (it.isSuccessful)
                        Utils.toast(applicationContext, "Password successfully changed", 0)
                    else
                        Utils.toast(
                            applicationContext,
                            "An error has occurred. Please try again later",
                            0
                        )
                }
            } else {
                Utils.toast(applicationContext, "Current Password does not match", 0)
            }
        }
    }
}