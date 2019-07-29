package com.sawankumarsingh.bookyourmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sawankumarsingh.bookyourmeal.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setContentView(R.layout.activity_splash_screen);

        int TIME_OUT = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent nextActivity = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                startActivity(nextActivity);
                finish();
            }
        }, TIME_OUT);
    }
}

