package com.example.miau.mvp30;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {
    final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String selected_color;
    String selected_idiom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Spinner color = findViewById (R.id.color_spinner);
        final Spinner idiom= findViewById (R.id.idiom_spinner);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String value = color.getSelectedItem().toString();
                editor.putString("color",value);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String restoredText = prefs.getString("text", null);
                if (restoredText != null) {
                   selected_color = prefs.getString("color", "No color defined");//"No name defined" is the default value.
            }
                //if(selected_color==)
        }

        });

        idiom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String value = idiom.getSelectedItem().toString();
                editor.putString("idiom",value);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String restoredText = prefs.getString("text", null);
                if (restoredText != null) {
                    selected_idiom = prefs.getString("idiom", "No idiom defined");//"No name defined" is the default value.
                }

            }

        });
    }
    }

