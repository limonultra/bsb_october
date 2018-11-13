package com.example.miau.mvp30

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import org.jetbrains.anko.defaultSharedPreferences


class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val toolbar = findViewById(R.id.toolbar4) as Toolbar
        setSupportActionBar(toolbar)
        Log.i("",resources.displayMetrics.densityDpi.toString())
        checkInternetPermission()
        if(Build.VERSION.SDK_INT>25){
            checkGPSPermission();}

        checkForTutorial()

        val bprof: ImageView = findViewById(R.id.bprof) as ImageView
        bprof.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, RoomCreate::class.java)
            startActivity(intent)
        }

        val bpup: ImageView = findViewById(R.id.bpup) as ImageView
        bpup.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, Access::class.java)
            startActivity(intent)
        }

        val bsm = findViewById (R.id.bsm) as ImageView
        bsm.setOnClickListener{
            val intent = Intent(this,SoloMode::class.java)
            startActivity(intent)
        }

        val info= findViewById(R.id.info) as ImageView
        info.setOnClickListener{
            val intent = Intent(this,TutorialActivity::class.java)
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
                val intent = Intent(this, SharedPreferences::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        return true
    }

    fun checkForTutorial() {
        val sharedPreferences = defaultSharedPreferences
        if (!sharedPreferences.getBoolean("tutorial", false)) {
            sharedPreferences.edit().putBoolean("tutorial", true).apply();
            val intent = Intent(this, TutorialActivity::class.java)
            intent.putExtra("First", true)
            startActivity(intent)
        }

    }
}
