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

class LoginActivity : AppCompatActivity() {
    lateinit var login:AppCompatButton
    lateinit var registerNow:TextView
    lateinit var loginEmail:EditText
    lateinit var loginPassword:EditText
    var emailPatern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+"
    var progressDialog: ProgressDialog? = null
    var nAuth: FirebaseAuth? = null
    var nUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login=findViewById(R.id.login)
        registerNow=findViewById(R.id.registerNow)
        loginEmail=findViewById(R.id.loginEmail)
        loginPassword=findViewById(R.id.loginPassword)
        progressDialog = ProgressDialog(this)
        nAuth = FirebaseAuth.getInstance()
        nUser = nAuth!!.currentUser

        login.setOnClickListener {
//            val intent= Intent(this,MainActivity::class.java)
//            startActivity(intent)
            perforLogin()
        }
        registerNow.setOnClickListener {
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun perforLogin() {
        val email: String = loginEmail.getText().toString()
        val password: String = loginPassword.getText().toString()
        if (!email.matches(emailPatern.toRegex())) {
            loginEmail.setError("Enter Correct Email")
        } else if (password.isEmpty() || password.length < 6) {
            loginPassword.setError("enter correct password")
        } else {
            progressDialog!!.setMessage("please wait while Login...")
            progressDialog!!.setTitle("Login")
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
            nAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog!!.dismiss()
                    setLoggedIn()
                    sendUserToNextActivity()
                    Toast.makeText(this, "login successfull", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    progressDialog!!.dismiss()
                    Toast.makeText(this, "" + task.exception, Toast.LENGTH_SHORT)
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
        Toast.makeText(this, "login true", Toast.LENGTH_SHORT).show()
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}