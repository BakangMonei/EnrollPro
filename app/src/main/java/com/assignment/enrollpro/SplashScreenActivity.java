package com.assignment.enrollpro;
/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */
// Splash Screen
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.assignment.enrollpro.Activities.OnboardScreenOne;

public class SplashScreenActivity extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Open OnboardScreenOne
                Intent x = new Intent(SplashScreenActivity.this, OnboardScreenOne.class);
                startActivity(x);
                finish();
            }
        }, 3000);
    }
}