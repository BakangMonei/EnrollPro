package com.assignment.enrollpro.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.assignment.enrollpro.Screens.LectureActivity;
import com.assignment.enrollpro.Authentications.LoginActivity;
import com.assignment.enrollpro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bttm_nav);
        fragmentContainer = findViewById(R.id.fragment_container);

        // Set the default activity
        launchLectureActivity();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    // Launch LectureActivity
                    launchLectureActivity();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Launch ProfileActivity
                    launchProfileActivity();
                    return true;
                } else if (item.getItemId() == R.id.nav_settings) {
                    // Launch SettingsActivity
                    launchSettingsActivity();
                    return true;
                } else if (item.getItemId() == R.id.nav_qrScanner) {
                    // Handle QR Scanner menu item click
                    Toast.makeText(MainActivity.this, "Scanner Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    // Dummy method to simulate login state, replace it with your actual login logic
    private boolean isLoggedIn() {
        // Return true if user is logged in, false otherwise
        return true; // Change this according to your logic
    }

    // Open Camera or QR Code Scanner Activity
    private void openQRCodeScanner() {
        // Start your QR Code Scanner activity here
        Toast.makeText(this, "Scanner Clicked", Toast.LENGTH_SHORT).show();
    }

    // Launch LectureActivity
    private void launchLectureActivity() {
        Intent intent = new Intent(MainActivity.this, LectureActivity.class);
        startActivity(intent);
    }

    // Launch ProfileActivity
    private void launchProfileActivity() {
//        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//        startActivity(intent);
    }

    // Launch SettingsActivity
    private void launchSettingsActivity() {
//        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//        startActivity(intent);
    }
}
