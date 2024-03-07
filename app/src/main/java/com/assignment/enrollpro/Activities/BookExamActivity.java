package com.assignment.enrollpro.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.Calendar;

public class BookExamActivity extends AppCompatActivity {

    private EditText moduleLeaderEmailTxt, moduleLeaderNameTxt, studentEmailTxt, studentIDNumberTxt, firstNameTxt,
            lastNameTxt, phoneNumberTxt, examRoomTxt, facultyTxt, moduleNameTxt, dateAndTimeEditText;

    private Button sendQRBtn, viewExamsBtn;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_exam);

        db = FirebaseFirestore.getInstance();

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

        // Create a new document with a generated ID
        // Create a new document with a generated ID
        db.collection("exams")
                .add(new BookExam(moduleLeaderEmail, moduleLeaderName, studentEmail, studentIDNumber, firstName, lastName,
                        phoneNumber, examRoom, faculty, moduleName, dateAndTime))
                .addOnSuccessListener(documentReference -> {
                    // On success, get the generated document ID
                    String documentId = documentReference.getId();

                    // Generate the URL using the document ID
                    String url = "https://enrollpro-d4377-default-rtdb.firebaseio.com?id=" + documentId;

                    // Send the URL as a text message
                    sendTextMessage(phoneNumber, url);

                    Toast.makeText(BookExamActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                    // Clear EditText fields after successful submission
                    clearEditTextFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookExamActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
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

    // Function to generate a link and send as SMS
    private void sendTextMessage(String phoneNumber, String message) {
        // Use Android's built-in SMS functionality to send the message
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }


}

//// Import the functions you need from the SDKs you need
//import { initializeApp } from "firebase/app";
//        import { getAnalytics } from "firebase/analytics";
//// TODO: Add SDKs for Firebase products that you want to use
//// https://firebase.google.com/docs/web/setup#available-libraries
//
//// Your web app's Firebase configuration
//// For Firebase JS SDK v7.20.0 and later, measurementId is optional
//        const firebaseConfig = {
//        apiKey: "AIzaSyBd-1L_b5TEE_PSuO4unXb3sLUNLybFeZ4",
//        authDomain: "enrollpro-d4377.firebaseapp.com",
//        databaseURL: "https://enrollpro-d4377-default-rtdb.firebaseio.com",
//        projectId: "enrollpro-d4377",
//        storageBucket: "enrollpro-d4377.appspot.com",
//        messagingSenderId: "540942520449",
//        appId: "1:540942520449:web:75fccae9773248fa0e7d80",
//        measurementId: "G-K401522SLK"
//        };
//
//// Initialize Firebase
//        const app = initializeApp(firebaseConfig);
//        const analytics = getAnalytics(app);