package com.assignment.enrollpro.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.enrollpro.Activities.BookExamActivity;
import com.assignment.enrollpro.R;

public class LectureActivity extends AppCompatActivity {

    private ImageView bookExamImageView, itemsUpdateImageView, itemsDeleteImageView, itemsViewImageView;
    private TextView bookExamTextView, itemsUpdateTextView, itemsDeleteTextView, itemsViewTextView;

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


    }

    public void goToBookExam(View view) {
        Intent intent = new Intent(LectureActivity.this, BookExamActivity.class);
        startActivity(intent);
    }
}