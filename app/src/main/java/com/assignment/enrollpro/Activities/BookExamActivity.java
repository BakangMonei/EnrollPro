package com.assignment.enrollpro.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import com.assignment.enrollpro.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;;
import java.util.Calendar;

public class BookExamActivity extends AppCompatActivity {

    // Lecture
    private Spinner moduleLeaderEmailTxt;
    private TextView moduleLeaderNameTxt;
    /***********************************/

    // Student
    private Spinner studentEmailTxt;
    private TextView studentIDNumberTxt, firstNameTxt, lastNameTxt, phoneNumberTxt;
    /***********************************/

    // Exam Room Spinner
    private Spinner examRoomTxt;
    /***********************************/

    // Faculty Spinner
    private Spinner facultyTxt;
    /***********************************/


    // Module Spinner
    private Spinner moduleNameTxt;



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
