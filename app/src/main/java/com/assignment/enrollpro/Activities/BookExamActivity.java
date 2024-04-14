package com.assignment.enrollpro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import com.assignment.enrollpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookExamActivity extends AppCompatActivity {

    // Lecture
    Spinner moduleLeaderEmailTxt;
    TextView moduleLeaderNameTxt;
    /***********************************/

    // Student
    Spinner studentEmailTxt;
    TextView studentIDNumberTxt, firstNameTxt, lastNameTxt, phoneNumberTxt;
    /***********************************/

    // Exam Room Spinner
    Spinner examRoomTxt;
    /***********************************/

    // Faculty Spinner
    Spinner facultyTxt;
    /***********************************/

    // Module Spinner
    Spinner moduleNameTxt;

    EditText dateAndTimeEditText;
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

        /**************************** ModuleLeader Spinner **********************************/
        moduleLeaderEmailTxt = (Spinner) findViewById(R.id.moduleLeaderEmailTxt);
        moduleLeaderNameTxt = (TextView) findViewById(R.id.moduleLeaderNameTxt);

        /******************************************************************************/


        /**************************** Student Spinner **********************************/
        studentEmailTxt = (Spinner) findViewById(R.id.studentEmailTxt);
        studentIDNumberTxt = (TextView) findViewById(R.id.studentIDNumberTxt);
        firstNameTxt = (TextView) findViewById(R.id.firstNameTxt);
        lastNameTxt = (TextView) findViewById(R.id.lastNameTxt);
        phoneNumberTxt = (TextView) findViewById(R.id.phoneNumberTxt);

        // Fetch emails from Firestore and populate spinner
        fetchEmails();

        studentEmailTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmail = parent.getItemAtPosition(position).toString();
                // Fetch data corresponding to selected email
                fetchData(selectedEmail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        /******************************************************************************/


        examRoomTxt = (Spinner) findViewById(R.id.examRoomTxt);
        facultyTxt = (Spinner) findViewById(R.id.facultyTxt);
        moduleNameTxt = (Spinner) findViewById(R.id.moduleNameTxt);


        dateAndTimeEditText = findViewById(R.id.dateAndTimeEditText);

        viewExamsBtn = findViewById(R.id.viewExamsBtn);
        sendQRBtn = findViewById(R.id.sendQRBtn);

        // Set click listener to show Date Picker Dialog
        dateAndTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        viewExamsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(BookExamActivity.this, ViewBookingExamActivity.class);
                startActivity(x);
            }
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


    /********************************* Student Details **************************************/
    private void fetchEmails() {
        // Fetch emails from Firestore
        db.collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> emailsList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming email is stored as a field in the document
                        String email = document.getString("email");
                        if (email != null && !email.isEmpty()) {
                            emailsList.add(email);
                        }
                    }
                    // Populate spinner with emails
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, emailsList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    studentEmailTxt.setAdapter(adapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching emails", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void fetchData(String email) {
        // Fetch data from FireStore corresponding to the selected email
        db.collection("students").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Assuming the document structure has fields like studentIDNumber, firstName, lastName, phoneNumber
                                String studentIDNumber = document.getString("studentIDNumber");
                                String firstName = document.getString("firstname");
                                String lastName = document.getString("lastname");
                                String phoneNumber = document.getString("phonenumber");

                                // Display fetched data in TextViews
                                studentIDNumberTxt.setText(studentIDNumber);
                                firstNameTxt.setText(firstName);
                                lastNameTxt.setText(lastName);
                                phoneNumberTxt.setText(phoneNumber);
                            }
                        } else {
                            Toast.makeText(BookExamActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /***********************************************************************/

    /********************************* ModuleLeader Details **************************************/
    private void fetchModuleLeaderEmails() {
        // Fetch emails from Firestore
        db.collection("lectures").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> emailsList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming email is stored as a field in the document
                        String email = document.getString("email");
                        if (email != null && !email.isEmpty()) {
                            emailsList.add(email);
                        }
                    }
                    // Populate spinner with emails
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, emailsList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    moduleLeaderEmailTxt.setAdapter(adapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching emails", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchModuleLeaderData(String email) {
        // Fetch data from Firestore corresponding to the selected email
        db.collection("lectures").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Assuming the document structure has fields like studentIDNumber, firstName, lastName, phoneNumber
                                String moduleLeaderNameTxt = document.getString("firstname" + " " + "lastname");

                                // Display fetched data in TextViews
                                studentIDNumberTxt.setText(moduleLeaderNameTxt);

                            }
                        } else {
                            Toast.makeText(BookExamActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /***********************************************************************/

}
