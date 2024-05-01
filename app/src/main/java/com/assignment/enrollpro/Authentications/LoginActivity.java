package com.assignment.enrollpro.Authentications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.*;
import android.widget.Toast;

import com.assignment.enrollpro.Model.UserDetails;
import com.assignment.enrollpro.R;
import com.assignment.enrollpro.Screens.LectureActivity;
import com.assignment.enrollpro.Screens.UserDashBoardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private TextView forgotPasswordTextView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginBtn);

        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(x);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query Firestore for document with matching email in the admin collection
        db.collection("admin").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot!= null &&!querySnapshot.isEmpty()) {
                        // Authenticate as admin
                        showUserDetailsDialog(email, password, LectureActivity.class);
                    } else {
                        // Check if email exists in lectures collection
                        db.collection("lectures").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot!= null &&!querySnapshot.isEmpty()) {
                                        // Authenticate as lectures
                                        showUserDetailsDialog(email, password, UserDashBoardActivity.class);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e(TAG, "Error getting documents", task.getException());
                                    Toast.makeText(LoginActivity.this, "Failed to retrieve documents", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Error getting documents", task.getException());
                    Toast.makeText(LoginActivity.this, "Failed to retrieve documents", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUserDetailsDialog(String email, String password, Class<?> dashboardActivity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_location, null);
        dialogBuilder.setView(dialogView);

        EditText locationEditText = dialogView.findViewById(R.id.locationEditText);
        EditText roomEditText = dialogView.findViewById(R.id.roomEditText);
        EditText timeEditText = dialogView.findViewById(R.id.timeEditText);
        EditText dateEditText = dialogView.findViewById(R.id.dateEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(view -> {
            String location = locationEditText.getText().toString().trim();
            String room = roomEditText.getText().toString().trim();
            String time = timeEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();

            if (TextUtils.isEmpty(location) || TextUtils.isEmpty(room) || TextUtils.isEmpty(time) || TextUtils.isEmpty(date)) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            signInWithEmailAndPassword(email, password, dashboardActivity, location, room, time, date);
            alertDialog.dismiss();
        });
    }

    private void signInWithEmailAndPassword(String email, String password, Class<?> dashboardActivity, String location, String room, String time, String date) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Store user details inFirestore
                        UserDetails userDetails = new UserDetails(location, room, time, date);
                        db.collection("admin_path_tracker").document(mAuth.getCurrentUser().getUid()).set(userDetails)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(LoginActivity.this, dashboardActivity));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to store user details", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}