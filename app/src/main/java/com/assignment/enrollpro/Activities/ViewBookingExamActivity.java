package com.assignment.enrollpro.Activities;
// Left is to do the Edit & Delete
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.enrollpro.Adapters.ViewBookingAdapter;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingExamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton, printFloatingActionButton;
    private ViewBookingAdapter adapter;
    private List<BookExam> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_exam);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookings = new ArrayList<>();
        adapter = new ViewBookingAdapter(bookings);
        recyclerView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            goToCreateBooking();
        });
        printFloatingActionButton = (FloatingActionButton) findViewById(R.id.printFloatingActionButton);
        printFloatingActionButton.setOnClickListener(view -> {
            Toast.makeText(ViewBookingExamActivity.this, "Print", Toast.LENGTH_SHORT).show();
        });

        // Fetch data from FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exams")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BookExam booking = document.toObject(BookExam.class);
                            bookings.add(booking);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void goToCreateBooking() {
        // Go to the AddBookingActivity
        Intent x = new Intent(ViewBookingExamActivity.this, BookExamActivity.class);
        Toast.makeText(ViewBookingExamActivity.this, "Create a Booking", Toast.LENGTH_SHORT).show();
        startActivity(x);
    }
}
