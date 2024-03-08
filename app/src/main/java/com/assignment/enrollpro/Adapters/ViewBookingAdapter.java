package com.assignment.enrollpro.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;

import java.util.List;

/**
* @Author: AMG
* @Date: Saturday 09/03/2024 March 2024
* @Time: 00:24
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

        }

        public void bind(BookExam booking) {
            studentEmailTextView.setText(booking.getStudentEmail());
            studentIDTextView.setText(booking.getStudentIDNumber());
            dateAndTimeTextView.setText(booking.getDateAndTime());
            moduleTextView.setText(booking.getModuleName());
        }
    }
}
