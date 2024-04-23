package com.assignment.enrollpro.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.*;

import com.assignment.enrollpro.R;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.*;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;;
import java.io.ByteArrayOutputStream;
import java.util.*;

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
    Spinner facultyTxt, roomSpinner, tableSpinner;
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
        // Add To FireStore
        sendQRBtn = (Button) findViewById(R.id.sendQRBtn);
        sendQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCodeAndSendToStudent();
            }
        });
        /**************************** ModuleLeader Spinner **********************************/
        moduleLeaderEmailTxt = (Spinner) findViewById(R.id.moduleLeaderEmailTxt);
        moduleLeaderNameTxt = (TextView) findViewById(R.id.moduleLeaderNameTxt);
        fetchLectureEmails();
        moduleLeaderEmailTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmail = parent.getItemAtPosition(position).toString();
                // Fetch data corresponding to selected email
                fetchLectureData(selectedEmail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        /******************************************************************************/

        /**************************** Student Spinner **********************************/
        studentEmailTxt = (Spinner) findViewById(R.id.studentEmailTxt);
        studentIDNumberTxt = (TextView) findViewById(R.id.studentIDNumberTxt);
        firstNameTxt = (TextView) findViewById(R.id.firstNameTxt);
        lastNameTxt = (TextView) findViewById(R.id.lastNameTxt);
        phoneNumberTxt = (TextView) findViewById(R.id.phoneNumberTxt);
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

        /***************************** Module Name Spinner **********************************/
        moduleNameTxt = (Spinner) findViewById(R.id.moduleNameTxt);
        fetchModuleLists();
        /******************************************************************************************/

        /***************************** Faculty Name Spinner **********************************/
        facultyTxt = (Spinner) findViewById(R.id.facultyTxt);
        fetchFacultyList();
        /******************************************************************************************/

        dateAndTimeEditText = findViewById(R.id.dateAndTimeEditText);

        /***************************** Room, Table, and Block Number Spinner **********************************/
        examRoomTxt = (Spinner) findViewById(R.id.examRoomTxt);
        roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
        tableSpinner = (Spinner) findViewById(R.id.tableSpinner);
        fetchBlockLists();

        // Set up listener for examRoomTxt spinner
        examRoomTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBlock = (String) parent.getItemAtPosition(position);
                fetchRoomsAndTablesForBlock(selectedBlock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        /******************************************************************************************/

        viewExamsBtn = findViewById(R.id.viewExamsBtn);

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
    private void fetchLectureEmails() {
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
                    Toast.makeText(BookExamActivity.this, "Error fetching lecture emails", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchLectureData(String email) {
        // Fetch data from FireStore corresponding to the selected email
        db.collection("lectures").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Assuming the document structure has fields like studentIDNumber, firstName, lastName, phoneNumber
                                String moduleLeader = document.getString("username");


                                // Display fetched data in TextViews
                                moduleLeaderNameTxt.setText(moduleLeader);
                            }
                        } else {
                            Toast.makeText(BookExamActivity.this, "Error fetching lecture data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /***********************************************************************/

    /********************************* Lecture Room Details **************************************/
    private void fetchRoomsAndTablesForBlock(String selectedBlock) {
        // Fetch rooms for the selected block from Firestore
        db.collection("roomTable").whereEqualTo("Block", selectedBlock).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> roomList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming Room is stored as an array in the document
                        List<String> rooms = (List<String>) document.get("Room");
                        if (rooms != null && !rooms.isEmpty()) {
                            roomList.addAll(rooms);
                        }
                    }
                    // Populate roomSpinner with rooms
                    ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, roomList);
                    roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    roomSpinner.setAdapter(roomAdapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching rooms for selected block", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Fetch tables for the selected block from Firestore
        db.collection("roomTable").whereEqualTo("Block", selectedBlock).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> tableList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming Table is stored as an array in the document
                        List<String> tables = (List<String>) document.get("Table");
                        if (tables != null && !tables.isEmpty()) {
                            tableList.addAll(tables);
                        }
                    }
                    // Populate tableSpinner with tables
                    ArrayAdapter<String> tableAdapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, tableList);
                    tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tableSpinner.setAdapter(tableAdapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching tables for selected block", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchBlockLists() {
        // Fetch blocklist from Firestore
        db.collection("roomTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> blockLists = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming Block is stored as a field in the document
                        String block = document.getString("Block");
                        if (block != null && !block.isEmpty()) {
                            blockLists.add(block);
                        }
                    }
                    // Populate spinner with blocks
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, blockLists);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    examRoomTxt.setAdapter(adapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching blockList", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listen for selection change on examRoomTxt spinner
        examRoomTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Fetch rooms and tables for the selected block
                String selectedBlock = (String) parentView.getItemAtPosition(position);
                fetchRoomsAndTablesForBlock(selectedBlock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }
    /***********************************************************************/


    /********************************* Module Lists **************************************/
    private void fetchModuleLists() {
        // Fetch modules from Firestore
        db.collection("module").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> moduleList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming email is stored as a field in the document
                        String moduleName = document.getString("moduleName");
                        if (moduleName != null && !moduleName.isEmpty()) {
                            moduleList.add(moduleName);
                        }
                    }
                    // Populate spinner with emails
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, moduleList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    moduleNameTxt.setAdapter(adapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching Module list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /***********************************************************************/

    /*********************************** Faculty lists ************************************/
    private void fetchFacultyList() {
        // Fetch modules from Firestore
        db.collection("faculty").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> facultyNameLists = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming email is stored as a field in the document
                        String faculty = document.getString("facultyName");
                        if (faculty != null && !faculty.isEmpty()) {
                            facultyNameLists.add(faculty);
                        }
                    }
                    // Populate spinner with emails
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BookExamActivity.this,
                            android.R.layout.simple_spinner_item, facultyNameLists);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    facultyTxt.setAdapter(adapter);
                } else {
                    Toast.makeText(BookExamActivity.this, "Error fetching facultyNameLists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /***********************************************************************/

    /***********************************************************************/
    protected void sendSMS(String qrCodeUrl) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Exam Details"
                + "\nStudent Email: " + studentEmailTxt
                + "\nStudent ID Number: " + studentIDNumberTxt
                + "\nExam Date: " + dateAndTimeEditText
                + "\nExam Block : " + examRoomTxt
                + "\nRoom: " + roomSpinner
                + "\nTable: " + tableSpinner
                + "\nQRCode URL: " + qrCodeUrl);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, "Share Exam Details");
        startActivity(shareIntent);
    }

    // Method to write data to Firestore
    private void writeDataToFirestore() {
        String email = studentEmailTxt.getSelectedItem().toString();
        String studentIDNumber = studentIDNumberTxt.getText().toString();
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();
        String moduleName = moduleNameTxt.getSelectedItem().toString();
        String dateAndTime = dateAndTimeEditText.getText().toString();
        String examRoom = examRoomTxt.getSelectedItem().toString();
        String room = roomSpinner.getSelectedItem().toString();
        String table = tableSpinner.getSelectedItem().toString();
        String faculty = facultyTxt.getSelectedItem().toString();
        String moduleLeaderEmail = moduleLeaderEmailTxt.getSelectedItem().toString();
        String moduleLeaderName = moduleLeaderNameTxt.getText().toString();

        // Validations
        if (email.isEmpty() || studentIDNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || moduleName.isEmpty() || dateAndTime.isEmpty() || examRoom.isEmpty() || room.isEmpty() || table.isEmpty() || faculty.isEmpty() || moduleLeaderEmail.isEmpty() || moduleLeaderName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Access a Cloud Firestore instance
        db = FirebaseFirestore.getInstance();

        // Create a new data object
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("studentIDNumber", studentIDNumber);
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("phoneNumber", phoneNumber);
        data.put("moduleName", moduleName);
        data.put("dateAndTime", dateAndTime);
        data.put("examRoom", examRoom);
        data.put("room", room);
        data.put("table", table);
        data.put("faculty", faculty);
        data.put("moduleLeaderEmail", moduleLeaderEmail);
        data.put("moduleLeaderName", moduleLeaderName);

        // Add a new document with a generated ID
        db.collection("exams")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(BookExamActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                        Toast.makeText(BookExamActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void generateQRCodeAndSendToStudent() {
        // Write data to Firestore
        writeDataToFirestore();

        // Generate QRCode data
        String studentEmail = studentEmailTxt.getSelectedItem().toString();
        String studentIDNumber = studentIDNumberTxt.getText().toString();
        String examDate = dateAndTimeEditText.getText().toString();
        String examRoom = examRoomTxt.getSelectedItem().toString();
        String room = roomSpinner.getSelectedItem().toString();
        String table = tableSpinner.getSelectedItem().toString();

        String qrCodeData = "Student Email: " + studentEmail + "\n"
                + "Student ID Number: " + studentIDNumber + "\n"
                + "Exam Date: " + examDate + "\n"
                + "Exam Room: " + examRoom + "\n"
                + "Room: " + room + "\n"
                + "Table: " + table;

        // Generate and store QRCode
        generateAndStoreQRCode(studentIDNumber, qrCodeData);
    }

    // Method to generate and store QRCode
    private void generateAndStoreQRCode(String studentIDNumber, String qrCodeData) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            // Store QRCode to Firebase Storage
            storeQRCodeToStorage(studentIDNumber, bitmap);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            Toast.makeText(BookExamActivity.this, "Error generating QRCode", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to send email to student
    private void sendEmailToStudent(String studentEmail, String qrCodeUrl) {
        // Create an Intent to send email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + studentEmail)); // Email address to send to

        // Set email subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your QR Code");

        // Set email body
        String emailBody = "Dear Student,\n\nPlease find your QR code attached below.\n\nQR Code URL: " + qrCodeUrl;
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            // Start the activity to send email
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            // Handle error if no email app is installed
            Toast.makeText(BookExamActivity.this, "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }
    // Method to store QRCode to Firebase Storage
    private void storeQRCodeToStorage(String studentIDNumber, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Get a reference to the location where we'll store our photos
        StorageReference storageRef = storage.getReference().child("qrcodes").child(studentIDNumber + ".png");

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // Continue with the task to get the download URL
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String qrCodeUrl = downloadUri.toString();

                // Send QRCode URL to student's email
                sendEmailToStudent(String.valueOf(studentEmailTxt), qrCodeUrl);
            } else {
                // Handle errors
                Toast.makeText(BookExamActivity.this, "Error storing QRCode", Toast.LENGTH_SHORT).show();
            }
        });
    }

}