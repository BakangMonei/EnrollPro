package com.assignment.enrollpro.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.assignment.enrollpro.Activities.DeletedBookingExamActivity;
import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageView unApprovedStudentImageView, approvedStudentsImageView;
    private TextView unApprovedStudentTextView, approvedStudentsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unApprovedStudentImageView = (ImageView) findViewById(R.id.unApprovedStudentImageView);
        approvedStudentsImageView = (ImageView)  findViewById(R.id.approvedStudentsImageView);

        unApprovedStudentTextView = (TextView) findViewById(R.id.unApprovedStudentTextView);
        approvedStudentsTextView = (TextView) findViewById(R.id.approvedStudentsTextView);

        unApprovedStudentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeleted();
            }
        });
        unApprovedStudentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeleted();
            }
        });

        approvedStudentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToApproved();
            }
        });
        approvedStudentsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToApproved();
            }
        });

    }

    protected void goToDeleted(){
        Intent intent = new Intent(MainActivity.this, DeletedBookingExamActivity.class);
        startActivity(intent);
    }

    protected void goToApproved(){
        Intent intent = new Intent(MainActivity.this, ViewBookingExamActivity.class);
        startActivity(intent);
    }
}
