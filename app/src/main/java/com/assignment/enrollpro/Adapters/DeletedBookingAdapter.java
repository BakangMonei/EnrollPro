package com.assignment.enrollpro.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;

import java.util.List;

public class DeletedBookingAdapter extends RecyclerView.Adapter<DeletedBookingAdapter.ViewHolder> {
    private List<BookExam> bookings;

    public DeletedBookingAdapter(List<BookExam> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public DeletedBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_deleted, parent, false);
        return new DeletedBookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedBookingAdapter.ViewHolder holder, int position) {
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

        Button restoreBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentEmailTextView = itemView.findViewById(R.id.viewStudentEmail);
            studentIDTextView = itemView.findViewById(R.id.viewStudentID);
            dateAndTimeTextView = itemView.findViewById(R.id.viewStudentDate);
            moduleTextView = itemView.findViewById(R.id.viewModule);
            restoreBtn = itemView.findViewById(R.id.restoreBtn);


            restoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Restore", Toast.LENGTH_SHORT).show();
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
