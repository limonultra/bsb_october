package com.example.miau.mvp30

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.support.v4.app.ActivityCompat
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.annotation.TargetApi
import android.Manifest.permission.INTERNET



class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        checkInternetPermission()
        checkGPSPermission()
        val bprof: ImageButton = findViewById(R.id.bprof) as ImageButton
        bprof.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, RoomCreate::class.java)
            startActivity(intent)
        }

        val bpup: ImageButton = findViewById(R.id.bpup) as ImageButton
        bpup.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, Access::class.java)
            startActivity(intent)
        }

    }

    fun checkInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            val tempPerms = arrayOf<String>(Manifest.permission.INTERNET)
            ActivityCompat.requestPermissions(this, tempPerms, 3)
        }
    }


    @TargetApi(26)
    fun checkGPSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val tempPerms = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, tempPerms, 4)
        }
    }
}
