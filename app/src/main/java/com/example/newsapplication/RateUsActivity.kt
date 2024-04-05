package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class RateUsActivity : AppCompatActivity() {
    lateinit var backBtn:ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_us)
        backBtn=findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

    }
}