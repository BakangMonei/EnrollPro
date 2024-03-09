package com.assignment.enrollpro.Adapters;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;

import java.util.List;

/**
 * @Author: One Kgarebe Lerole
 * @Date: February 2024
 * @Time: 10:00 am
 * @Location: University Of Botswana, Gaborone, Botswana
 */

public class ViewBookingAdapter extends RecyclerView.Adapter<ViewBookingAdapter.ViewHolder> {
    private List<BookExam> bookings;

    public ViewBookingAdapter(List<BookExam> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_view_exam_bookings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookExam booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentEmailTextView;
        TextView studentIDTextView;
        TextView dateAndTimeTextView;
        TextView moduleTextView;

        Button viewDeleteBtn, editStudentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentEmailTextView = itemView.findViewById(R.id.viewStudentEmail);
            studentIDTextView = itemView.findViewById(R.id.viewStudentID);
            dateAndTimeTextView = itemView.findViewById(R.id.viewStudentDate);
            moduleTextView = itemView.findViewById(R.id.viewModule);
            viewDeleteBtn = itemView.findViewById(R.id.viewDeleteBtn);
            editStudentBtn = itemView.findViewById(R.id.editStudentBtn);

            viewDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            });

            editStudentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookExam booking = bookings.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), ViewBookingExamActivity.class);
//                    intent.putExtra("booking", booking);
                    v.getContext().startActivity(intent);
                }
            });

        }

        public void bind(BookExam booking) {
            studentEmailTextView.setText(booking.getStudentEmail());
            studentIDTextView.setText(booking.getStudentIDNumber());
            dateAndTimeTextView.setText(booking.getDateAndTime());
            moduleTextView.setText(booking.getModuleName());
        }
    }
}
