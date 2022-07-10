package com.example.faceattendancesystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.Admin.AddSubject;
import com.example.faceattendancesystem.DataHelper.Courses;
import com.example.faceattendancesystem.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.viewHolder>
{

    private ArrayList<Courses> Items;
    private Context context;
    public AdminCourseAdapter(ArrayList<Courses> items, Context context) {
        Items = items;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_available_courses,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        holder.coureName.setText("Name: "+Items.get(position).getCourseName());
        holder.courseCode.setText("Code: "+Items.get(position).getCourseCode());
        holder.courseFaculty.setText("Faculty: "+Items.get(position).getCourseFaculty());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddSubject.class);
                intent.putExtra("courseName",Items.get(position).getCourseName());
                intent.putExtra("courseCode",Items.get(position).getCourseCode());
                intent.putExtra("courseID",Items.get(position).getCourseID());
                intent.putExtra("courseFaculty",Items.get(position).getCourseFaculty());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView coureName,courseCode,courseFaculty;
        public Button btnRegister;
        public RelativeLayout layout;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            coureName = itemView.findViewById(R.id.view_course_name);
            courseCode = itemView.findViewById(R.id.view_course_code);
            courseFaculty = itemView.findViewById(R.id.view_course_faculty);
            layout = itemView.findViewById(R.id.linearv);
        }
    }
}
