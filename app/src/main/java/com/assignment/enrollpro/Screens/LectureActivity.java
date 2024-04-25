package com.assignment.enrollpro.Screens;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class LectureActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    ImageView ScanImageView, itemsViewImageView;
    FirebaseFirestore db;

    TextView  emailTextView, usernameTextView;
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bttm_nav);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        // Get current user's email and username from Firestore
        getCurrentUserDetails();

        itemsViewImageView = (ImageView) findViewById(R.id.itemsViewImageView);
        itemsViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureActivity.this, ViewBookingExamActivity.class);
                startActivity(intent);

            }
        });
        ScanImageView = (ImageView) findViewById(R.id.ScanImageView);

        ScanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LectureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LectureActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                } else {
                    initiateScan();
                }
            }
        });
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
                            Toast.makeText(LectureActivity.this, "Added to approved collection", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LectureActivity.this, "Failed to add to approved collection", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LectureActivity.this, "QR code deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to delete QR code file
                Toast.makeText(LectureActivity.this, "Failed to delete QR code", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LectureActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LectureActivity.this, "Failed to add to reject collection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getCurrentUserDetails() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("admin").document(currentUserId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String email = document.getString("email");
                        String firstName = document.getString("firstname");
                        String lastName = document.getString("lastname");

                        emailTextView.setText(email);
                        usernameTextView.setText("Welcome: " + firstName + " " + lastName);
                    } else {
                        // If the user is not found in the admin collection, check the lectures collection
                        DocumentReference userRef1 = db.collection("lectures").document(currentUserId);
                        userRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        String email = document.getString("email");
                                        String firstName = document.getString("firstname");
                                        String lastName = document.getString("lastname");

                                        emailTextView.setText(email);
                                        usernameTextView.setText("Welcome: " + firstName + " " + lastName);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}