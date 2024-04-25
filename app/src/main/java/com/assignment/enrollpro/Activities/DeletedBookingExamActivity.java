package com.assignment.enrollpro.Activities;
/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.enrollpro.Adapters.DeletedBookingAdapter;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;
import com.assignment.enrollpro.Utils.DeleteSwipeToSelectCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class DeletedBookingExamActivity extends AppCompatActivity {

    private static final String TAG = "DeletedBookingExamActivity";

    private RecyclerView recyclerView;
    private DeletedBookingAdapter adapterD;
    private List<BookExam> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_booking_exam);

        recyclerView = findViewById(R.id.recyclerDeletedBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookings = new ArrayList<>();
        adapterD = new DeletedBookingAdapter(bookings);
        recyclerView.setAdapter(adapterD);



        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deleted")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BookExam booking = document.toObject(BookExam.class);
                            bookings.add(booking);
                        }
                        adapterD.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        // Attach swipe-to-select functionality to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DeleteSwipeToSelectCallback(adapterD));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}