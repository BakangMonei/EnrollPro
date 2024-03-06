package com.assignment.enrollpro.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.enrollpro.R;

public class LectureActivity extends AppCompatActivity {

    private ImageView itemsPurchasedImageView, itemsUpdateImageView, itemsDeleteImageView, itemsViewImageView;
    private TextView itemsPurchasedTextView, itemsUpdateTextView, itemsDeleteTextView, itemsViewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
    }
}