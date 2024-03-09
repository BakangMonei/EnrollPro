package com.assignment.enrollpro.Activities;
// This is where we view 'Deleted Records' and be able to restore them


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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


public class DeletedBookingExamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewBookingAdapter adapter;
    private List<BookExam> bookings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_booking_exam);

        recyclerView = findViewById(R.id.recyclerDeletedBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookings = new ArrayList<>();
        adapter = new ViewBookingAdapter(bookings);
        recyclerView.setAdapter(adapter);

        // Fetch data from FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("examsDeleted")
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
}