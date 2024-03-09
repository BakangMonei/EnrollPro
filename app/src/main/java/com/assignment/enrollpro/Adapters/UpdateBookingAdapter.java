package com.assignment.enrollpro.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;

import java.util.List;

/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */

public class UpdateBookingAdapter extends RecyclerView.Adapter<UpdateBookingAdapter.ViewHolder>{
    private List<BookExam> bookings;
    private Context context;

    public UpdateBookingAdapter(Context context, List<BookExam> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    // Add a method to add a single booking
    public void addBooking(BookExam booking) {
        bookings.add(booking);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpdateBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_view_exam_bookings, parent, false);
        return new UpdateBookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateBookingAdapter.ViewHolder holder, int position) {
        BookExam booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText moduleLeaderEmailTxt, moduleLeaderNameTxt, studentEmailTxt, studentIDNumberTxt, firstNameTxt,
                lastNameTxt, phoneNumberTxt, examRoomTxt, facultyTxt, moduleNameTxt, dateAndTimeEditText;

        Button updateBtn, deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleLeaderEmailTxt = itemView.findViewById(R.id.moduleLeaderEmailTxt);
            moduleLeaderNameTxt = itemView.findViewById(R.id.moduleLeaderNameTxt);
            studentEmailTxt = itemView.findViewById(R.id.studentEmailTxt);
            studentIDNumberTxt = itemView.findViewById(R.id.studentIDNumberTxt);
            firstNameTxt = itemView.findViewById(R.id.firstNameTxt);
            lastNameTxt = itemView.findViewById(R.id.lastNameTxt);
            phoneNumberTxt = itemView.findViewById(R.id.phoneNumberTxt);
            examRoomTxt = itemView.findViewById(R.id.examRoomTxt);
            facultyTxt = itemView.findViewById(R.id.facultyTxt);
            moduleNameTxt = itemView.findViewById(R.id.moduleNameTxt);
            dateAndTimeEditText = itemView.findViewById(R.id.dateAndTimeEditText);

            updateBtn = itemView.findViewById(R.id.updateBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Update Button Clicked", Toast.LENGTH_SHORT).show();
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Delete Button Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(BookExam booking) {

        }
    }
}
