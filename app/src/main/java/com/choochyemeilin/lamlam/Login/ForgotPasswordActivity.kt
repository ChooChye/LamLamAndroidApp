package com.choochyemeilin.lamlam.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choochyemeilin.lamlam.R

import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*


class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText

    private lateinit var resetPasswordBtn: Button
    private lateinit var cancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        emailEt = findViewById(R.id.editText_forgotpw_email)

        resetPasswordBtn = findViewById(R.id.button_forgotpw_resetpw)
        cancel = findViewById(R.id.button_forgotpw_cancel)

        cancel.setOnClickListener {
            finish()
        }
        resetPasswordBtn.setOnClickListener {
            var email: String = emailEt.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        }

        auth.currentUser?.updatePassword(editTextTextPassword_forgotpw_password.text.toString())
            ?.addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password changes successfully", Toast.LENGTH_LONG)
                        .show()
                    finish()
                } else {
                    Toast.makeText(this, "password not changed", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }


}