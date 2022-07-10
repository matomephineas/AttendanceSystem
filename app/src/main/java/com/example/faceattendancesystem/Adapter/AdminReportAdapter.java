package com.example.faceattendancesystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.Admin.GenerateTextFile;
import com.example.faceattendancesystem.DataHelper.AttendanceReport;
import com.example.faceattendancesystem.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminReportAdapter extends RecyclerView.Adapter<AdminReportAdapter.viewHolder> {

    private ArrayList<AttendanceReport> Items;
    private Context context;

    public AdminReportAdapter(ArrayList<AttendanceReport> items, Context context) {
        Items = items;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_student_view_report,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        holder.stdnt_email.setText(Items.get(position).getStudentEmail());
        holder.stdnt_att_date.setText("Date: "+Items.get(position).getAttendanceDate());
        holder.student_number.setText("Student Number: "+Items.get(position).getStudentNo());
        holder.subject_att_name.setText("Subject name: "+Items.get(position).getSubjectName());
        holder.atndceStatus.setText("Status: "+Items.get(position).getAttendanceStatus());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GenerateTextFile.class);
                intent.putExtra("email",Items.get(position).getStudentEmail());
                intent.putExtra("studentnumber",Items.get(position).getStudentNo());
                intent.putExtra("date",Items.get(position).getAttendanceDate());
                intent.putExtra("subjectname",Items.get(position).getSubjectName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private TextView stdnt_email,stdnt_att_date,student_number,subject_att_name,atndceStatus;
        private RelativeLayout layout;
        public viewHolder(@NonNull @com.google.firebase.database.annotations.NotNull View itemView) {
            super(itemView);
            stdnt_email = itemView.findViewById(R.id.stdnt_email);
            stdnt_att_date = itemView.findViewById(R.id.stdnt_att_date);
            student_number = itemView.findViewById(R.id.student_number);
            subject_att_name = itemView.findViewById(R.id.subject_att_name);
            atndceStatus = itemView.findViewById(R.id.atndceStatus);
            layout = itemView.findViewById(R.id.layout4);
        }
    }
}
