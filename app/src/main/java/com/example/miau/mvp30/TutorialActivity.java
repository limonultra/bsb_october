package com.example.miau.mvp30;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.miau.mvp30.Adapter.TutorialPagerAdapter;

public class TutorialActivity extends AppCompatActivity {

    private  boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TutorialPagerAdapter(this, getIntent().getBooleanExtra("First", false)));



    }

    @Override
    public void onBackPressed() {
        if(!firstTime){
        super.onBackPressed();
        }
    }
}


