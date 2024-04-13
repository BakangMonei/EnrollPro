package com.assignment.enrollpro.Activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
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

    private EditText dateAndTimeEditText;
    Button sendQRBtn, viewExamsBtn;

    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_exam);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize views


        dateAndTimeEditText = findViewById(R.id.dateAndTimeEditText);

        viewExamsBtn = findViewById(R.id.viewExamsBtn);
        sendQRBtn = findViewById(R.id.sendQRBtn);

        // Set click listener to show Date Picker Dialog
        dateAndTimeEditText.setOnClickListener(v -> showDateTimePicker());


        viewExamsBtn.setOnClickListener(v -> {
            Intent x = new Intent(BookExamActivity.this, ViewBookingExamActivity.class);
            startActivity(x);
        });

    }


    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int minute = currentDate.get(Calendar.MINUTE);

        // Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            currentDate.set(year1, monthOfYear, dayOfMonth);
            // Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(BookExamActivity.this, (view1, hourOfDay, minute1) -> {
                currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                currentDate.set(Calendar.MINUTE, minute1);
                // Update EditText with selected date and time
                updateDateTimeEditText(currentDate);
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDateTimeEditText(Calendar calendar) {
        // Update EditText with selected date and time
        dateAndTimeEditText.setText(calendar.getTime().toString());
    }

}
