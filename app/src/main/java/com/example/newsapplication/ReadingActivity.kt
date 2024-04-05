package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapplication.DataClass.Data1
import com.squareup.picasso.Picasso

class ReadingActivity : AppCompatActivity() {
    lateinit var imageHeadline:ImageView
    lateinit var titleTextView:TextView
    lateinit var authortextView:TextView
    lateinit var DateTextView:TextView
    lateinit var descriptionTextView:TextView
    lateinit var contentTextView:TextView
    lateinit var backBtn:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        imageHeadline=findViewById(R.id.imageHeadline)
        titleTextView=findViewById(R.id.titleTextView)
        authortextView=findViewById(R.id.authortextView)
        DateTextView=findViewById(R.id.DateTextView)
        descriptionTextView=findViewById(R.id.descriptionTextView)
        contentTextView=findViewById(R.id.contentTextView)
        backBtn=findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
        // Get intent data
        // Retrieve article data from intent extras
        val author = intent.getStringExtra("author")
        val content = intent.getStringExtra("content")
        val description = intent.getStringExtra("description")
        val publishedAt = intent.getStringExtra("publishedAt")
        val title = intent.getStringExtra("title")
        val urlToImage = intent.getStringExtra("urlToImage")

        // Display article data in views
        titleTextView.text = title
        authortextView.text = author
        DateTextView.text = publishedAt
        descriptionTextView.text = description
        contentTextView.text = content
        // Load image using Picasso or any other image loading library
        Picasso.get().load(urlToImage).into(imageHeadline)

    }
}