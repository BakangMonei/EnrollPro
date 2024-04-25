package com.assignment.enrollpro.Activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.assignment.enrollpro.Adapters.ViewBookingAdapter;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;
import com.assignment.enrollpro.Screens.LectureActivity;
import com.assignment.enrollpro.Utils.SwipeToSelectCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import com.assignment.enrollpro.Adapters.ViewBookingAdapter;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;
import com.assignment.enrollpro.Screens.LectureActivity;
import com.assignment.enrollpro.Utils.SwipeToSelectCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ViewBookingExamActivity extends AppCompatActivity {

    private static final String TAG = "ViewBookingExamActivity";

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton, printFloatingActionButton;
    private ViewBookingAdapter adapter;
    private List<BookExam> bookings;

    private Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_exam);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookings = new ArrayList<>();
        adapter = new ViewBookingAdapter(bookings);
        recyclerView.setAdapter(adapter);

        actionButton = findViewById(R.id.actionButton);
        actionButton.setOnClickListener(view -> {
            // Perform action on selected items
            List<BookExam> selectedBookings = new ArrayList<>();
            for (Integer position : adapter.getSelectedItems()) {
                selectedBookings.add(bookings.get(position));
            }
            // Perform action using selectedBookings list
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            goToCreateBooking();
        });

        printFloatingActionButton = findViewById(R.id.printFloatingActionButton);
        printFloatingActionButton.setOnClickListener(view -> {
            Toast.makeText(ViewBookingExamActivity.this, "Print", Toast.LENGTH_SHORT).show();
        });

        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("approved")
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

        // Attach swipe-to-select functionality to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToSelectCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void goToCreateBooking() {
        Intent intent = new Intent(ViewBookingExamActivity.this, BookExamActivity.class);
        Toast.makeText(ViewBookingExamActivity.this, "Creating booking", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}