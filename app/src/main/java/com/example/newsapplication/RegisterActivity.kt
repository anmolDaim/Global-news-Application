package com.example.newsapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    lateinit var registerlogin:AppCompatButton
    lateinit var loginNow:TextView
    lateinit var termsBtn:TextView
    lateinit var inputEmail:EditText
    lateinit var inputPassword:EditText
    lateinit var inputConfirmPassword:EditText
    var emailPatern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+"
    var progressDialog: ProgressDialog? = null
    var nAuth: FirebaseAuth? = null
    var nUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerlogin=findViewById(R.id.registerlogin)
        loginNow=findViewById(R.id.loginNow)
        termsBtn=findViewById(R.id.termsBtn)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.confirmInputPassword)
        progressDialog = ProgressDialog(this)

        nAuth = FirebaseAuth.getInstance()
        nUser = nAuth!!.currentUser

        registerlogin.setOnClickListener {
//            val intent =Intent(this,LoginActivity::class.java)
//            startActivity(intent)
            PerForAuth()
        }
        loginNow.setOnClickListener {
            val intent =Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        termsBtn.setOnClickListener {
            val intent =Intent(this,TermsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun PerForAuth() {
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        val confirmPassword = inputConfirmPassword.text.toString()
        if (!email.matches(emailPatern.toRegex())) {
            inputEmail.error = "Enter Correct Email"
        } else if (password.isEmpty() || password.length < 6) {
            inputPassword.error = "enter correct password"
        } else if (password != confirmPassword) {
            inputConfirmPassword.error = "password dont match both fields"
        } else {
            progressDialog!!.setMessage("please wit while registraton...")
            progressDialog!!.setTitle("Registration")
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
            nAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog!!.dismiss()
                    setLoggedIn()
                    sendUserToNextActivity()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration successfull",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressDialog!!.dismiss()
                    Toast.makeText(this@RegisterActivity, "" + task.exception, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun setLoggedIn() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
        Toast.makeText(this, "register true", Toast.LENGTH_SHORT).show()
    }
    private fun sendUserToNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}