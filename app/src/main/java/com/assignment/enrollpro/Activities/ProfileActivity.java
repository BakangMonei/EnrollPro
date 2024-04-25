package com.assignment.enrollpro.Activities;

/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.enrollpro.Authentications.LoginActivity;
import com.assignment.enrollpro.R;
import com.assignment.enrollpro.Screens.LectureActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail, tvFirstname, tvLastname, tvPhoneNumber, tvUsername, tvModules;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;

    ImageView back_btn;
    private static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(ProfileActivity.this, LectureActivity.class);
                startActivity(x);
            }
        });

        tvEmail = findViewById(R.id.tv_email);
        tvFirstname = findViewById(R.id.tv_firstname);
        tvLastname = findViewById(R.id.tv_lastname);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvUsername = findViewById(R.id.tv_username);
        tvModules = findViewById(R.id.tv_modules);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        bottomNavigationView = findViewById(R.id.bttm_nav_lec);
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

                    showLogoutDialog();
                    return true;
                } else if (item.getItemId() == R.id.nav_qrScanner) {
                    // Handle QR Scanner menu item click
                    Toast.makeText(ProfileActivity.this, "Scanner Clicked", Toast.LENGTH_SHORT).show();

                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    } else {
                        initiateScan();
                    }
                    return true;
                }
                return false;
            }
        });



        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("admin").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the user's data from the document
                        String email = document.getString("email");
                        String firstname = document.getString("firstname");
                        String lastname = document.getString("lastname");
                        String phoneNumber = document.getString("phoneNumber");
                        String username = document.getString("username");
                        String lecture = document.getString("lecture");
                        String[] modules = (String[]) document.get("modules");

                        // Display the user's data in the UI
                        tvEmail.setText("Email: " + email);
                        tvFirstname.setText("Firstname: " + firstname);
                        tvLastname.setText("Lastname: " + lastname);
                        tvPhoneNumber.setText("Phone Number: " + phoneNumber);
                        tvUsername.setText("Username: " + username);

                        if (lecture!= null) {
                            tvModules.setText("Lecture: " + lecture);
                        } else {
                            StringBuilder modulesText = new StringBuilder("Modules: ");
                            for (String module : modules) {
                                modulesText.append(module).append(", ");
                            }
                            tvModules.setText(modulesText.toString());
                        }
                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            }
        });
    }

    private void openQRCodeScanner() {
        Toast.makeText(ProfileActivity.this, "Scanner Clicked", Toast.LENGTH_SHORT).show();
    }



    private void launchLectureActivity() {
        Toast.makeText(ProfileActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
        Intent x = new Intent(ProfileActivity.this, LectureActivity.class);
        startActivity(x);
    }
    private void launchProfileActivity() {
        Toast.makeText(ProfileActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();

    }

    private void launchSettingsActivity() {
        Toast.makeText(ProfileActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
    }


    // Add this method to show the logout dialog box
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        builder.show();
    }
    // Logout Function
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


    private void initiateScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("QR Code Data");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle VERIFY button click
                        String qrCodeData = result.getContents();
                        addToApprovedCollection(qrCodeData);
                        deleteQRCode(qrCodeData);
                    }
                });
                builder.setNegativeButton("REJECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle REJECT button click
                        String qrCodeData = result.getContents();
                        addToDeletedCollection(qrCodeData);
                    }
                });
                builder.create().show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addToApprovedCollection(String qrCodeData) {
        // Parse the QR code data into a map
        Map<String, String> qrCodeMap = parseQRCodeData(qrCodeData);

        // Create a new map for the approved data
        Map<String, Object> approvedData = new HashMap<>();
        approvedData.put("dateAndTime", String.valueOf(System.currentTimeMillis()));
        approvedData.put("examRoom", qrCodeMap.get("Room"));
        approvedData.put("faculty", qrCodeMap.get("Faculty"));
        approvedData.put("firstName", qrCodeMap.get("First Name"));
        approvedData.put("lastName", qrCodeMap.get("Last Name"));
        approvedData.put("moduleLeaderEmail", qrCodeMap.get("Module Leader Email"));
        approvedData.put("moduleLeaderName", qrCodeMap.get("Module Leader Name"));
        approvedData.put("moduleName", qrCodeMap.get("Module Name"));
        approvedData.put("phoneNumber", qrCodeMap.get("Phone Number"));
//        approvedData.put("qrCodeImage", qrCodeMap.get("qrCodeImage"));
        approvedData.put("studentEmail", qrCodeMap.get("Student Email"));
        approvedData.put("studentIDNumber", qrCodeMap.get("Student ID Number"));
        approvedData.put("table", qrCodeMap.get("Table"));

        // Add the approved data to Firestore
        db.collection("approved")
                .add(approvedData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Added to approved collection", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to add to approved collection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to parse the QR code data into a map
    private Map<String, String> parseQRCodeData(String qrCodeData) {
        Map<String, String> qrCodeMap = new HashMap<>();

        // Split the QR code data by newline characters
        String[] lines = qrCodeData.split("\\r?\\n");

        // Loop through the lines and add them to the map
        for (String line : lines) {
            String[] keyValue = line.split(":");
            if (keyValue.length == 2) {
                qrCodeMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return qrCodeMap;
    }

    private void deleteQRCode(String qrCodeData) {
        // Get a reference to the QR code file in Firebase Storage
        StorageReference qrCodeRef = FirebaseStorage.getInstance().getReference().child("/" + qrCodeData);

        // Delete the QR code file
        qrCodeRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // QR code file deleted successfully
                Toast.makeText(ProfileActivity.this, "QR code deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to delete QR code file
                Toast.makeText(ProfileActivity.this, "Failed to delete QR code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateScan();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Store to Delete
    private void addToDeletedCollection(String qrCodeData) {
        // Parse the QR code data into a map
        Map<String, String> qrCodeMap = parseQRCodeData(qrCodeData);

        // Create a new map for the approved data
        Map<String, Object> approvedData = new HashMap<>();
        approvedData.put("dateAndTime", String.valueOf(System.currentTimeMillis()));
        approvedData.put("examRoom", qrCodeMap.get("Room"));
        approvedData.put("faculty", qrCodeMap.get("Faculty"));
        approvedData.put("firstName", qrCodeMap.get("First Name"));
        approvedData.put("lastName", qrCodeMap.get("Last Name"));
        approvedData.put("moduleLeaderEmail", qrCodeMap.get("Module Leader Email"));
        approvedData.put("moduleLeaderName", qrCodeMap.get("Module Leader Name"));
        approvedData.put("moduleName", qrCodeMap.get("Module Name"));
        approvedData.put("phoneNumber", qrCodeMap.get("Phone Number"));
        approvedData.put("studentEmail", qrCodeMap.get("Student Email"));
        approvedData.put("studentIDNumber", qrCodeMap.get("Student ID Number"));
        approvedData.put("table", qrCodeMap.get("Table"));

        // Add the deleted data to Firestore
        db.collection("deleted")
                .add(approvedData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to add to reject collection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}