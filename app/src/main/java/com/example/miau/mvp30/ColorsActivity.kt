package com.example.miau.mvp30

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
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

        val toolbar = findViewById(R.id.toolbarc) as Toolbar
        setSupportActionBar(toolbar)

        initColor()
    }

    private fun initColor() {
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        layout.setBackgroundColor(Color.parseColor(pref.getString("background", "#ffffff")))
        setTextColor(pref.getInt("text", ContextCompat.getColor(this, R.color.black)))
        putShapeColors(pref.getInt("pos", 1))

        if (pref.getString("background", "#ffffff").equals("#111111"))
            changeShapeWhite()
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
            1 -> {
                pref.edit().putString("background", "#ffffff").putInt("pos", pos).apply()
                setTextColor(Color.parseColor("#005466"))
                changeShapeBlue()
            }
            2 -> {
                pref.edit().putString("background", "#111111").putInt("pos", pos).apply()
                setTextColor(Color.parseColor("#ffffff"))
                changeShapeWhite()
            }
            3 -> {
                pref.edit().putString("background", "#ffeb4e").putInt("pos", pos).apply()
                setTextColor(Color.parseColor("#111111"))
                changeShapeBlack()
            }
            4 -> {
                pref.edit().putString("background", "#ff0014").putInt("pos", pos).apply()
                setTextColor(Color.parseColor("#111111"))
                changeShapeBlack()
            }
            5 -> {
                pref.edit().putString("background", "#4c8c28").putInt("pos", pos).apply()
                setTextColor(Color.parseColor("#111111"))
                changeShapeBlack()
            }
        }

        putShapeColors(pos)
        layout.setBackgroundColor(Color.parseColor(pref.getString("background", "#ffffff")))
    }

    private fun changeShapeWhite() {
        val textView = findViewById<TextView>(R.id.a_button1);
        val textView2 = findViewById<TextView>(R.id.a_button2);
        val textView3 = findViewById<TextView>(R.id.a_button3);
        val textView4 = findViewById<TextView>(R.id.a_button4);
        val textView5 = findViewById<TextView>(R.id.a_button5);

        textView.setBackgroundResource(R.drawable.a_button_white)
        textView2.setBackgroundResource(R.drawable.a_button_white)
        textView3.setBackgroundResource(R.drawable.a_button_white)
        textView4.setBackgroundResource(R.drawable.a_button_white)
        textView5.setBackgroundResource(R.drawable.a_button_white)

        val imageView = findViewById<ImageView>(R.id.imageView8)
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gotadecolor_light_gray))
    }

    private fun changeShapeBlue() {
        val textView = findViewById<TextView>(R.id.a_button1);
        val textView2 = findViewById<TextView>(R.id.a_button2);
        val textView3 = findViewById<TextView>(R.id.a_button3);
        val textView4 = findViewById<TextView>(R.id.a_button4);
        val textView5 = findViewById<TextView>(R.id.a_button5);

        textView.setBackgroundResource(R.drawable.a_button)
        textView2.setBackgroundResource(R.drawable.a_button)
        textView3.setBackgroundResource(R.drawable.a_button)
        textView4.setBackgroundResource(R.drawable.a_button)
        textView5.setBackgroundResource(R.drawable.a_button)

        val imageView = findViewById<ImageView>(R.id.imageView8)
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gotadecolor))
    }

    private fun changeShapeBlack() {
        val textView = findViewById<TextView>(R.id.a_button1);
        val textView2 = findViewById<TextView>(R.id.a_button2);
        val textView3 = findViewById<TextView>(R.id.a_button3);
        val textView4 = findViewById<TextView>(R.id.a_button4);
        val textView5 = findViewById<TextView>(R.id.a_button5);

        textView.setBackgroundResource(R.drawable.a_button_grey)
        textView2.setBackgroundResource(R.drawable.a_button_grey)
        textView3.setBackgroundResource(R.drawable.a_button_grey)
        textView4.setBackgroundResource(R.drawable.a_button_grey)
        textView5.setBackgroundResource(R.drawable.a_button_grey)

        val imageView = findViewById<ImageView>(R.id.imageView8)
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gotacolor_negro))
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

    fun putShapeColors(pos: Int) {
        val textView = findViewById<TextView>(R.id.a_button1);
        val textView2 = findViewById<TextView>(R.id.a_button2);
        val textView3 = findViewById<TextView>(R.id.a_button3);
        val textView4 = findViewById<TextView>(R.id.a_button4);
        val textView5 = findViewById<TextView>(R.id.a_button5);

        textView.visibility = View.VISIBLE
        textView2.visibility = View.VISIBLE
        textView3.visibility = View.VISIBLE
        textView4.visibility = View.VISIBLE
        textView5.visibility = View.VISIBLE

        when (pos) {
            1 -> {
                textView.setTextColor(Color.parseColor("#005466"))
                textView2.setTextColor(Color.parseColor("#111111"))
                textView3.setTextColor(Color.parseColor("#ff0014"))
                textView4.setTextColor(Color.parseColor("#4c8c28"))
                textView5.visibility = View.INVISIBLE

            }
            2 -> {
                textView.setTextColor(Color.parseColor("#ffffff"))
                textView2.setTextColor(Color.parseColor("#00b6c7"))
                textView3.setTextColor(Color.parseColor("#ffeb4e"))
                textView4.setTextColor(Color.parseColor("#ff0014"))
                textView5.setTextColor(Color.parseColor("#4c8c28"))
            }
            3 -> {
                textView.setTextColor(Color.parseColor("#111111"))
                textView2.visibility = View.INVISIBLE
                textView3.visibility = View.INVISIBLE
                textView4.visibility = View.INVISIBLE
                textView5.visibility = View.INVISIBLE

            }
            4 -> {
                textView.setTextColor(Color.parseColor("#111111"))
                textView2.visibility = View.INVISIBLE
                textView3.visibility = View.INVISIBLE
                textView4.visibility = View.INVISIBLE
                textView5.visibility = View.INVISIBLE

            }
            5 -> {
                textView.setTextColor(Color.parseColor("#ffffff"))
                textView2.setTextColor(Color.parseColor("#111111"))
                textView3.visibility = View.INVISIBLE
                textView4.visibility = View.INVISIBLE
                textView5.visibility = View.INVISIBLE

            }
        }
    }
}
