package com.assignment.enrollpro.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.enrollpro.Activities.BookExamActivity;
import com.assignment.enrollpro.Activities.DeletedBookingExamActivity;
import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

//    private FloatingActionButton uploadCSVFloatingActionBtn;
    private FirebaseFirestore db;
    /*************************************/

    private ImageView bookExamImageView, itemsViewImageView;
    private TextView bookExamTextView, itemsViewTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        bookExamImageView = (ImageView) findViewById(R.id.bookExamImageView);
        bookExamTextView = (TextView) findViewById(R.id.bookExamTextView);

        db = FirebaseFirestore.getInstance();

//        uploadCSVFloatingActionBtn = (FloatingActionButton) findViewById(R.id.uploadCSVFloatingActionBtn);
//        uploadCSVFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestStoragePermissionAndPickCSV();
//            }
//        });

        bookExamImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBookExam(v);
            }
        });

        bookExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBookExam(v);
            }
        });

        itemsViewImageView = (ImageView) findViewById(R.id.itemsViewImageView);
        itemsViewTextView = (TextView) findViewById(R.id.itemsViewTextView);
        itemsViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        itemsViewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void goToBookExam(View view) {
        Intent intent = new Intent(LectureActivity.this, BookExamActivity.class);
        startActivity(intent);
    }

    public void goToViewBooking(View view) {
        Intent intent = new Intent(LectureActivity.this, ViewBookingExamActivity.class);
        startActivity(intent);
    }

    public void goToDeletedBooking(View view) {
        Intent intent = new Intent(LectureActivity.this, DeletedBookingExamActivity.class);
        startActivity(intent);
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
    /******************************************************/
}