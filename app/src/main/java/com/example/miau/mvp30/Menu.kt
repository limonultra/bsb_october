package com.example.miau.mvp30

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.os.Build
import android.util.Log
import android.view.MenuItem


class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        Log.i("",resources.displayMetrics.densityDpi.toString())
        checkInternetPermission()
        if(Build.VERSION.SDK_INT>25){
            checkGPSPermission();}
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


    fun checkGPSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val tempPerms = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, tempPerms, 4)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
