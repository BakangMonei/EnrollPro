package com.assignment.enrollpro.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.assignment.enrollpro.Activities.ViewBookingExamActivity;
import com.assignment.enrollpro.Model.BookExam;
import com.assignment.enrollpro.R;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingAdapter extends RecyclerView.Adapter<ViewBookingAdapter.ViewHolder> {
    private List<BookExam> bookings;
    private List<Integer> selectedItems = new ArrayList<>();


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

    // Add this method to return the selected items
    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BookExam booking = bookings.get(position);
        holder.bind(booking);
        holder.itemView.setActivated(selectedItems.contains(position));
        holder.checkBox.setChecked(selectedItems.contains(position));
        holder.checkBox.setVisibility(selectedItems.size() > 0 ? View.VISIBLE : View.GONE);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggleSelection(position);
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.size() > 0) {
                    toggleSelection(position);
                } else {
                    // Handle regular click action
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(Integer.valueOf(position));
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }



    public void showOptions(View itemView, int position) {
        // Show options for the swiped item
        Toast.makeText(itemView.getContext(), "Options for item " + position, Toast.LENGTH_SHORT).show();
    }


    public void deleteItem(int position) {
        bookings.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentEmailTextView;
        TextView studentIDTextView;
        TextView dateAndTimeTextView;
        TextView moduleTextView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentEmailTextView = itemView.findViewById(R.id.viewStudentEmail);
            studentIDTextView = itemView.findViewById(R.id.viewStudentID);
            dateAndTimeTextView = itemView.findViewById(R.id.viewStudentDate);
            moduleTextView = itemView.findViewById(R.id.viewModule);
            checkBox = itemView.findViewById(R.id.checkBox);

            // Set click listener for checkbox
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelection(getAdapterPosition());
                }
            });

            // Set click listener for regular item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedItems.size() > 0) {
                        toggleSelection(getAdapterPosition());
                    } else {
                        // Handle regular click action
                    }
                }
            });
        }

        public void bind(BookExam booking) {
            studentEmailTextView.setText(booking.getStudentEmail());
            studentIDTextView.setText(booking.getStudentIDNumber());
            dateAndTimeTextView.setText(booking.getDateAndTime());
            moduleTextView.setText(booking.getModuleName());

            // Update checkbox state based on selectedItems
            checkBox.setChecked(selectedItems.contains(getAdapterPosition()));
        }
    }

}