package com.example.miau.mvp30.Fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.miau.mvp30.R
import kotlinx.android.synthetic.main.activity_transcription.*


class TranscriptionDFragment(): DialogFragment() {

    private var rootView: View? = null
    private var salto_linea = "\n"
    private lateinit var toolbar: Toolbar
    private var textColor: Int = 0
    private var backColor: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_transcription_prof, container, false)
            toolbar = rootView?.findViewById(R.id.toolbar3) as Toolbar

            val colorSharedPref = inflater.context.getSharedPreferences("colors", Context.MODE_PRIVATE)
            backColor = colorSharedPref.getString("background", "#f4f4f4")
            textColor = colorSharedPref.getInt("text", ContextCompat.getColor(inflater.context, R.color.black))



            toolbar.inflateMenu(R.menu.menu_profesor)
            toolbar.setOnMenuItemClickListener(
                    Toolbar.OnMenuItemClickListener { item ->
                        val id = item.itemId

                        if (id == R.id.transcription) {
                            // handle close button click here
                            dismiss()
                            return@OnMenuItemClickListener true
                        }
                        false
                    })

        }


        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateDialog(savedInstanceState)
    }


    fun escribirSubs(message: String, newtext: String) {
        profText.text = Editable.Factory.getInstance().newEditable("$newtext $message")
        profText.setBackgroundColor(Color.parseColor(backColor))
        profText.setTextColor(textColor)
    }

    fun appendSalto(newtext: String) {
        if (!profText.text.endsWith("\n") || profText.text.length > 0)
            profText.text = Editable.Factory.getInstance().newEditable("$newtext $salto_linea")
    }


    companion object {
        private val TAG = "Access"

    }
}