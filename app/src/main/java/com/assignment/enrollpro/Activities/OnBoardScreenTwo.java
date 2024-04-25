package com.assignment.enrollpro.Activities;
/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.assignment.enrollpro.Authentications.LoginActivity;
import com.assignment.enrollpro.R;

public class OnBoardScreenTwo extends AppCompatActivity {

    private TextView nextBtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_screen_two);

        nextBtn2 = (TextView) findViewById(R.id.nextBtn2);
        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(OnBoardScreenTwo.this, LoginActivity.class);
                startActivity(x);
            }
        });
    }
}