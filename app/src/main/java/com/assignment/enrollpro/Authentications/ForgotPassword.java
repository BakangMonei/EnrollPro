package com.assignment.enrollpro.Authentications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.assignment.enrollpro.R;

public class ForgotPassword extends AppCompatActivity {

    private EditText editText_email;
    private Button button_reset_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editText_email = (EditText) findViewById(R.id.editText_email);
        button_reset_password = (Button) findViewById(R.id.button_reset_password);
    }
}