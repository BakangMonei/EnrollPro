package com.assignment.enrollpro.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.enrollpro.Activities.BookExamActivity;
import com.assignment.enrollpro.Activities.DeletedBookingExamActivity;
import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.R;

public class LectureActivity extends AppCompatActivity {

    private ImageView bookExamImageView, itemsUpdateImageView, itemsDeletedImageView, itemsViewImageView;
    private TextView bookExamTextView, itemsUpdateTextView, itemsDeletedTextView, itemsViewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        bookExamImageView = (ImageView) findViewById(R.id.bookExamImageView);
        bookExamTextView = (TextView) findViewById(R.id.bookExamTextView);

        bookExamImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBookExam(v);
            }
        });

        bookExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBookExam(v);
            }
        });

        itemsViewImageView = (ImageView) findViewById(R.id.itemsViewImageView);
        itemsViewTextView = (TextView) findViewById(R.id.itemsViewTextView);
        itemsViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewBooking(v);
            }
        });
        itemsViewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewBooking(v);
            }
        });

        itemsDeletedImageView = (ImageView) findViewById(R.id.itemsDeletedImageView);
        itemsDeletedTextView = (TextView) findViewById(R.id.itemsDeletedTextView);
        itemsDeletedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeletedBooking(v);
            }
        });
        itemsDeletedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeletedBooking(v);
            }
        });

    }

    public void goToBookExam(View view) {
        Intent intent = new Intent(LectureActivity.this, BookExamActivity.class);
        startActivity(intent);
    }

    public void goToViewBooking(View view) {
        Intent intent = new Intent(LectureActivity.this, ViewBookingExamActivity.class);
        startActivity(intent);
    }

    public void goToDeletedBooking(View view) {
        Intent intent = new Intent(LectureActivity.this, DeletedBookingExamActivity.class);
        startActivity(intent);
    }
}