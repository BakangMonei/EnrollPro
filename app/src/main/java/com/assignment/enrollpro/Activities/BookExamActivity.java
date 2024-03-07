package com.assignment.enrollpro.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;


import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

public class BookExamActivity extends AppCompatActivity {

    private EditText moduleLeaderEmailTxt, moduleLeaderNameTxt, studentEmailTxt, studentIDNumberTxt, firstNameTxt,
            lastNameTxt, phoneNumberTxt, examRoomTxt, facultyTxt, moduleNameTxt, dateAndTimeEditText;

    private Button sendQRBtn, viewExamsBtn;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_exam);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        moduleLeaderEmailTxt = (EditText) findViewById(R.id.moduleLeaderEmailTxt);
        moduleLeaderNameTxt = (EditText) findViewById(R.id.moduleLeaderNameTxt);
        studentEmailTxt = (EditText) findViewById(R.id.studentEmailTxt);
        studentIDNumberTxt = (EditText) findViewById(R.id.studentIDNumberTxt);
        firstNameTxt = (EditText) findViewById(R.id.firstNameTxt);
        lastNameTxt = (EditText) findViewById(R.id.lastNameTxt);
        phoneNumberTxt = (EditText) findViewById(R.id.phoneNumberTxt);
        examRoomTxt = (EditText) findViewById(R.id.examRoomTxt);
        facultyTxt = (EditText) findViewById(R.id.facultyTxt);
        moduleNameTxt = (EditText) findViewById(R.id.moduleNameTxt);
        dateAndTimeEditText = (EditText) findViewById(R.id.dateAndTimeEditText);

        viewExamsBtn = (Button) findViewById(R.id.viewExamsBtn);
        sendQRBtn = (Button) findViewById(R.id.sendQRBtn);

        // Set click listener to show Date Picker Dialog
        dateAndTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        sendQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToFirestore();
            }
        });

    }

    private void clearEditTextFields() {
        moduleLeaderEmailTxt.setText("");
        moduleLeaderNameTxt.setText("");
        studentEmailTxt.setText("");
        studentIDNumberTxt.setText("");
        firstNameTxt.setText("");
        lastNameTxt.setText("");
        phoneNumberTxt.setText("");
        examRoomTxt.setText("");
        facultyTxt.setText("");
        moduleNameTxt.setText("");
        dateAndTimeEditText.setText("");
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int minute = currentDate.get(Calendar.MINUTE);

        // Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                currentDate.set(year, monthOfYear, dayOfMonth);
                // Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookExamActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentDate.set(Calendar.MINUTE, minute);
                        // Update EditText with selected date and time
                        updateDateTimeEditText(currentDate);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDateTimeEditText(Calendar calendar) {
        // Update EditText with selected date and time
        dateAndTimeEditText.setText(calendar.getTime().toString());
    }


    private void sendDataToFirestore() {
        String moduleLeaderEmail = moduleLeaderEmailTxt.getText().toString();
        String moduleLeaderName = moduleLeaderNameTxt.getText().toString();
        String studentEmail = studentEmailTxt.getText().toString();
        String studentIDNumber = studentIDNumberTxt.getText().toString();
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();
        String examRoom = examRoomTxt.getText().toString();
        String faculty = facultyTxt.getText().toString();
        String moduleName = moduleNameTxt.getText().toString();
        String dateAndTime = dateAndTimeEditText.getText().toString();

        // Generate QR Code
        String qrCodeValue = UUID.randomUUID().toString(); // Generate a random unique value
        Bitmap qrBitmap = generateQRCode(qrCodeValue);

        // Upload QR Code to Firebase Storage
        uploadQRCodeToStorage(qrBitmap, qrCodeValue);

        // Send SMS
        sendSMS(phoneNumber, "Here is the link to your QR Code: <link_to_qr_code>");

        // Create a new document with a generated ID
        db.collection("exams")
                .add(new BookExam(moduleLeaderEmail, moduleLeaderName, studentEmail, studentIDNumber, firstName, lastName,
                        phoneNumber, examRoom, faculty, moduleName, dateAndTime, qrCodeValue))
                .addOnSuccessListener(documentReference -> {
                    // On success, get the generated document ID
                    String documentId = documentReference.getId();
                    Toast.makeText(BookExamActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                    // Clear EditText fields after successful submission
                    clearEditTextFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookExamActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
                });
    }

    private Bitmap generateQRCode(String value) {
        // Append the data to the URL
        String url = "https://yourdomain.com/yourpath/" + value;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            Log.e("QRCode", "Error generating QR code: " + e.getMessage());
            return null;
        }
    }


    private void uploadQRCodeToStorage(Bitmap bitmap, String qrCodeValue) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Set the path of where the image will be stored in Firebase Storage
        String path = "qr_codes/" + qrCodeValue + ".png";

        // Get a reference to the storage location and upload the image
        StorageReference qrCodeRef = storage.getReference().child(path);
        qrCodeRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Log.d("UploadQRCode", "QR code uploaded successfully");
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    Log.e("UploadQRCode", "Failed to upload QR code: " + exception.getMessage());
                });
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            smsIntent.putExtra("sms_body", message);
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }





}
