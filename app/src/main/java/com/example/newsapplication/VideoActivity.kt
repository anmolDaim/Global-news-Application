package com.example.newsapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        // Assuming you have TextViews in your layout to display these
        val videoHeadingTextView: TextView = findViewById(R.id.videoHeadingTextView)
        val videoDateTextView: TextView = findViewById(R.id.videoDateTextView)
        val videoContentTextView: TextView = findViewById(R.id.videoContentTextView)
        val videoImageView: ImageView = findViewById(R.id.videoImageView)
        val backBtn: ImageView = findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }
        videoImageView.setOnClickListener {
            val intent= Intent(this@VideoActivity,ExoPlayerActivity::class.java)
            startActivity(intent)
        }

        // Retrieve the data passed through the intent
        val videoHeading = intent.getStringExtra("VIDEO_HEADING")
        val videoDate = intent.getStringExtra("VIDEO_DATE")
        val videoContent = intent.getStringExtra("VIDEO_CONTENT")
        val videoImageUrl = intent.getStringExtra("VIDEO_IMAGE_URL")

        // Set the retrieved data to the TextViews
        videoHeadingTextView.text = videoHeading
        videoDateTextView.text = videoDate
        videoContentTextView.text=videoContent
        // Use Picasso to load the image from the URL into the ImageView
        if (!videoImageUrl.isNullOrEmpty()) {
            Picasso.get().load(videoImageUrl).into(videoImageView)
        }
    }
}