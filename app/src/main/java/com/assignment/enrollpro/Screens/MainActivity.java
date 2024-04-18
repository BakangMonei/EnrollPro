package com.assignment.enrollpro.Screens;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.assignment.enrollpro.Activities.OnboardScreenOne;
import com.assignment.enrollpro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bttm_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    // Handle Home menu item click
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Handle Profile menu item click
                    return true;
                } else if (item.getItemId() == R.id.nav_settings) {
                    // Handle Settings menu item click
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            // Handle Home menu item click
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            // Handle Profile menu item click
            return true;
        } else if (item.getItemId() == R.id.nav_settings) {
            // Handle Settings menu item click
            return true;
        } else if (item.getItemId() == R.id.nav_qrScanner) {
            // Handle QR Scanner menu item click
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Open Camera

}