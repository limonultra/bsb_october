package com.example.miau.mvp30;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.LTGRAY;
import static android.graphics.Color.MAGENTA;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

public class Settings extends PreferenceActivity {
    final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String selected_color;
    String selected_idiom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.layout.activity_settings);


    }
    }

