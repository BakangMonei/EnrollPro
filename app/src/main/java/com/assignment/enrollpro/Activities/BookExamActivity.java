package com.assignment.enrollpro.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;


import com.assignment.enrollpro.R;

import java.util.Calendar;

public class BookExamActivity extends AppCompatActivity {

    private EditText moduleLeaderEmailTxt, moduleLeaderNameTxt, studentEmailTxt, studentIDNumberTxt, firstNameTxt,
            lastNameTxt, phoneNumberTxt, examRoomTxt, facultyTxt, moduleNameTxt, dateAndTimeEditText;

    private Button sendQRBtn, viewExamsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_exam);

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
}