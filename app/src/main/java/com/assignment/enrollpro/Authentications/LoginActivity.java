package com.assignment.enrollpro.Authentications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.assignment.enrollpro.R;
import com.assignment.enrollpro.Screens.LectureActivity;
import com.assignment.enrollpro.Screens.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private CheckBox rememberMeCheckBox;
    private EditText emailEditText, editTextTextPassword;
    private Button loginBtn;

    private TextView forgotPasswordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLecture(v);
            }
        });

    }

    public void goToLecture(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}