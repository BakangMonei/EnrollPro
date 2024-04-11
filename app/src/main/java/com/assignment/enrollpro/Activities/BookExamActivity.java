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

        moduleLeaderEmailTxt = findViewById(R.id.moduleLeaderEmailTxt);
        moduleLeaderNameTxt = findViewById(R.id.moduleLeaderNameTxt);
        studentEmailTxt = findViewById(R.id.studentEmailTxt);
        studentIDNumberTxt = findViewById(R.id.studentIDNumberTxt);
        firstNameTxt = findViewById(R.id.firstNameTxt);
        lastNameTxt = findViewById(R.id.lastNameTxt);
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        examRoomTxt = findViewById(R.id.examRoomTxt);
        facultyTxt = findViewById(R.id.facultyTxt);
        moduleNameTxt = findViewById(R.id.moduleNameTxt);
        dateAndTimeEditText = findViewById(R.id.dateAndTimeEditText);

        viewExamsBtn = findViewById(R.id.viewExamsBtn);
        sendQRBtn = findViewById(R.id.sendQRBtn);

        // Set click listener to show Date Picker Dialog
        dateAndTimeEditText.setOnClickListener(v -> showDateTimePicker());

        sendQRBtn.setOnClickListener(v -> sendDataToFirestore());

        viewExamsBtn.setOnClickListener(v -> {
            Intent x = new Intent(BookExamActivity.this, ViewBookingExamActivity.class);
            startActivity(x);
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
        generateAndSendQRCode(qrCodeValue, studentEmail, phoneNumber);

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

                    // To immediately view the booking, navigate to the ViewBookingExamActivity
                    Intent x = new Intent(BookExamActivity.this, ViewBookingExamActivity.class);
                    startActivity(x);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookExamActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
                });
    }

    private void generateAndSendQRCode(String qrCodeValue, String studentEmail, String phoneNumber) {
        // Generate QR code
        Bitmap qrBitmap = generateQRCodeBitmap(qrCodeValue);

        // Send QR code to student's email
        sendEmailWithQRCode(qrBitmap, studentEmail);

        // Generate dynamic link of QR code and send it as SMS text
        generateDynamicLinkAndSendSMS(qrCodeValue, phoneNumber);
    }

    private Bitmap generateQRCodeBitmap(String qrCodeValue) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeValue, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendEmailWithQRCode(Bitmap qrBitmap, String studentEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("image/*");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{studentEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "QR Code for " + studentEmail);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find your QR code for exam booking attached.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(qrBitmap));
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private void generateDynamicLinkAndSendSMS(String qrCodeValue, String phoneNumber) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://enrollpro.page.link/?qrCodeValue=" + qrCodeValue))
                .setDomainUriPrefix("https://enrollpro.page.link")
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri shortLink = task.getResult().getShortLink();
                        String smsMessage = "Your exam QR code: " + shortLink.toString();
                        sendSMS(phoneNumber, smsMessage);
                    }
                });
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QR_Code", null);
        return Uri.parse(path);
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
