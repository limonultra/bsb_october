package com.example.miau.mvp30

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView


class ColorsActivity : AppCompatActivity() {

    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors)

        pref = getSharedPreferences("colors", Context.MODE_PRIVATE)

        arrayListOf<String>("#005466", "#111111", "#ff0014", "4c8c28") //WhitePattern
        arrayListOf<String>("#ffffff", "#00b6c7", "#ffeb4e", "#ff0014", "#4c8c28") // blackPattern
        arrayListOf<String>("#111111") //yellowPattern
        arrayListOf<String>("#111111") //redPattern
        arrayListOf<String>("#ffffff", "#111111") //greenPattern

        val toolbar = findViewById(R.id.toolbarc) as Toolbar
        setSupportActionBar(toolbar)
    }

    fun onClickBackground(v: View) {
        val imageView = (v as ImageView)
        setBackgroundColor(Integer.parseInt(imageView.contentDescription.toString()))
    }


    fun onClickFont(v: View) {
        setTextColor((v as TextView).currentTextColor)
    }

    fun setBackgroundColor(pos: Int) {
        val layout = findViewById<ConstraintLayout>(R.id.layout)

        when (pos) {
            1 -> pref.edit().putString("background", "#f4f4f4").apply()
            2 -> pref.edit().putString("background", "#111111").apply()
            3 -> pref.edit().putString("background", "#ffeb4e").apply()
            4 -> pref.edit().putString("background", "#ff0014").apply()
            5 -> pref.edit().putString("background", "#4c8c28").apply()
        }

        layout.setBackgroundColor(Color.parseColor(pref.getString("background", "#f4f4f4")))
    }

    fun setTextColor(color: Int) {
        pref.edit().putInt("text", color).apply()
        changeTextsColor(color)
    }

    fun changeTextsColor(color: Int) {
        val textView1 = findViewById<TextView>(R.id.textView14)
        val textView2 = findViewById<TextView>(R.id.textView15)
        val textView3 = findViewById<TextView>(R.id.textView16)
        val textView4 = findViewById<TextView>(R.id.textView17)

        textView1.setTextColor(color)
        textView2.setTextColor(color)
        textView3.setTextColor(color)
        textView4.setTextColor(color)
    }
}

