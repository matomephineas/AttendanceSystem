package com.example.faceattendancesystem.Admin;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class AdminViewCourse extends RecyclerView.Adapter<AdminViewCourse.viewholder>{


    @NonNull
    @NotNull
    @Override
    public viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class viewholder extends RecyclerView.ViewHolder
    {
        public viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
