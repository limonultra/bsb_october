package com.example.miau.mvp30

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.example.miau.mvp30.utils.AppPreferences
import android.webkit.WebView



class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        AppPreferences.init(this)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById(R.id.toolbarS) as Toolbar
        setSupportActionBar(toolbar)
        if (!AppPreferences.firstRun) {
            AppPreferences.firstRun = true
            Log.d("SpinKotlin", "The value of our pref is: ${AppPreferences.firstRun}")
        }

        val color_button: Button = findViewById(R.id.color_button) as Button
        color_button.setOnClickListener {
            // Handler code here.
            setContentView(R.layout.activity_transcription)
        }

        val idiom_button: Button = findViewById(R.id.idiom_button) as Button
        idiom_button.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, IdiomaActivity::class.java)
            startActivity(intent)
        }

        val privacity_button: Button= findViewById(R.id.privacity_button) as Button
        privacity_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse("http://docs.google.com/viewer?url=" + "https://www.talentumtelefonica.com/POL%C3%8DTICA%20DE%20PROTECCI%C3%93N%20DE%20DATOS.pdf"), "text/html")
            startActivity(intent)
        }

        val facebook: ImageButton= findViewById(R.id.facebook) as ImageButton
        facebook.setOnClickListener {
            // Handler code here.
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/somosTalentum/"))
            startActivity(intent)
        }

        val twitter: ImageButton= findViewById(R.id.twitter) as ImageButton
        twitter.setOnClickListener {
            // Handler code here.
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/talentum"))
            startActivity(intent)
        }

        val instagram: ImageButton= findViewById(R.id.instagram) as ImageButton
        instagram.setOnClickListener {
            // Handler code here.
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/somostalentum/"))
            startActivity(intent)
        }


    }
}