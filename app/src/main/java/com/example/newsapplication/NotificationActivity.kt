package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Adapter.notificationAdapter
import com.example.newsapplication.DataBase.NotificationDbHelper
import com.example.newsapplication.DataClass.notificationDataClass

class NotificationActivity : AppCompatActivity() {
    lateinit var notificationRecyclerView: RecyclerView
    lateinit var toolbar_image:ConstraintLayout
    private lateinit var dbHelper: NotificationDbHelper
    private lateinit var adapter: notificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)



        notificationRecyclerView = findViewById(R.id.notificationRecyclerView)
        toolbar_image=findViewById(R.id.backBtn)

        toolbar_image.setOnClickListener {
            finish()
        }

        dbHelper = NotificationDbHelper(this)
        adapter = notificationAdapter(dbHelper.getAllNotifications())

        val recyclerView: RecyclerView = findViewById(R.id.notificationRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}