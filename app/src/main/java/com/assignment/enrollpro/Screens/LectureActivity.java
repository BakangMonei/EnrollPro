package com.assignment.enrollpro.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LectureActivity extends AppCompatActivity {

    /*********** CSV Operations ***********/
    private static final int PICK_CSV_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;


    private ImageView ScanImageView, itemsViewImageView;
    private TextView scanQRTextView, itemsViewTextView;


    //    private FloatingActionButton uploadCSVFloatingActionBtn;
    private FirebaseFirestore db;

    /*************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        db = FirebaseFirestore.getInstance();


        itemsViewImageView = (ImageView) findViewById(R.id.itemsViewImageView);

        itemsViewTextView = (TextView) findViewById(R.id.itemsViewTextView);

        itemsViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExams();
            }
        });



        ScanImageView = (ImageView) findViewById(R.id.ScanImageView);
        scanQRTextView = (TextView) findViewById(R.id.scanQRTextView);

        ScanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    startQRScanner();
                } else {
                    requestCameraPermission();
                }
            }
        });


//        uploadCSVFloatingActionBtn = (FloatingActionButton) findViewById(R.id.uploadCSVFloatingActionBtn);
//        uploadCSVFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestStoragePermissionAndPickCSV();
//            }
//        });


    }

    /***************** CSV Operations ********************/
    private void requestStoragePermissionAndPickCSV() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            pickCSVFile();
        }
    }

    private void pickCSVFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/csv");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_CSV_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickCSVFile();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRScanner();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    uploadCSVToFirestore(uri);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Logic for handling the captured image
        } else {
            // Handle QR code scanning result
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    String qrCodeData = result.getContents();
                    fetchDetailsFromFirestore(qrCodeData);
                } else {
                    Toast.makeText(this, "Failed to scan QR code", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void uploadCSVToFirestore(Uri csvUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(csvUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Assuming the CSV format matches the provided model
                if (data.length == 11) {
                    uploadDocumentToFirestore(data);
                }
            }
        } catch (IOException e) {
            Log.e("UploadCSV", "Error reading CSV", e);
        }
    }

    private void uploadDocumentToFirestore(String[] data) {
        CollectionReference collectionReference = db.collection("your_collection_name");

        // Map CSV data to Firestore fields
        // Assuming the order of fields matches the CSV columns
        // Change field names as needed
        DocumentReference documentReference = collectionReference.document();
        Map<String, Object> documentData = new HashMap<>();
        documentData.put("moduleLeaderEmail", data[0]);
        documentData.put("moduleLeaderName", data[1]);
        documentData.put("studentEmail", data[2]);
        documentData.put("studentIDNumber", data[3]);
        documentData.put("firstName", data[4]);
        documentData.put("lastName", data[5]);
        documentData.put("phoneNumber", data[6]);
        documentData.put("examRoom", data[7]);
        documentData.put("faculty", data[8]);
        documentData.put("moduleName", data[9]);
        documentData.put("dateAndTime", data[10]);

        documentReference.set(documentData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UploadCSV", "Document uploaded successfully");
                        } else {
                            Log.e("UploadCSV", "Error uploading document", task.getException());
                        }
                    }
                });
    }

    /****************************************************/

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }


    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a QR code");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }


    private void fetchDetailsFromFirestore(String qrCodeData) {
        // Assuming the QR code data is the document ID in Firestore
        db.collection("your_collection_name")
                .document(qrCodeData)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                displayDetailsDialog(document.getData());
                            } else {
                                Toast.makeText(LectureActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("Firestore", "Error getting document", task.getException());
                        }
                    }
                });
    }

    private void displayDetailsDialog(Map<String, Object> details) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details");
        StringBuilder detailsText = new StringBuilder();
        for (Map.Entry<String, Object> entry : details.entrySet()) {
            detailsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        builder.setMessage(detailsText.toString());
        builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle approve action
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    protected void goToExams() {
        Intent intent = new Intent(LectureActivity.this, ViewBookingExamActivity.class);
        startActivity(intent);
        finish();
    }
}