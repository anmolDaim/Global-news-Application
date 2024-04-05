package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView=findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener ( navListener )

        // Display initial fragment or perform initial action
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            HomeFragment()
        ).commit()

    }
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = HomeFragment()
            R.id.navigation_discover -> selectedFragment = DiscoverFragment()
            R.id.navigation_saved -> selectedFragment = SavedFragment()
            R.id.navigation_profile -> selectedFragment = ProfileFragment()
            R.id.navigation_video -> selectedFragment = VideoFragment()
        }
        // Replace the current fragment with the selected one
        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, selectedFragment)
                .commit()
            return@OnNavigationItemSelectedListener true
        }
        false
    }


}