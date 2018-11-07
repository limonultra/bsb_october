package com.example.miau.mvp30;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {
    final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Spinner color = findViewById (R.id.color_spinner);
        final Spinner idiom= findViewById (R.id.idiom_spinner);

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String MY_PREFS_NAME = "MyPrefsFile";
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                String value = color.getSelectedItem().toString();
                editor.putString("color",value);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("color", color.getSelectedItem().toString());
                if (restoredText != null) {
                    String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            }

        }

        });

        idiom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String MY_PREFS_NAME = "MyPrefsFile";
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                String value = idiom.getSelectedItem().toString();
                editor.putString("color",value);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("text", null);
                if (restoredText != null) {
                    String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                }

            }

        });
    }
    }

