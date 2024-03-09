package com.assignment.enrollpro.Activities;
// Done With This Screen

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.assignment.enrollpro.Authentications.LoginActivity;
import com.assignment.enrollpro.R;

public class OnboardScreenOne extends AppCompatActivity {

    private TextView skipTxt;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_screen_one);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        skipTxt = (TextView) findViewById(R.id.skipTxt);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open OnboardScreenTwo
                Intent x = new Intent(OnboardScreenOne.this, OnBoardScreenTwo.class);
                startActivity(x);
            }
        });

        skipTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open HomeActivity
                Intent x = new Intent(OnboardScreenOne.this, LoginActivity.class);
                startActivity(x);
            }
        });
    }
}