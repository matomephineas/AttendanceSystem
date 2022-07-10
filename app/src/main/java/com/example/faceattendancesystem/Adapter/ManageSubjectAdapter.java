package com.example.faceattendancesystem.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.Admin.AddTimeTable;
import com.example.faceattendancesystem.Admin.Lactures;
import com.example.faceattendancesystem.DataHelper.Subject;
import com.example.faceattendancesystem.DataHelper.User;
import com.example.faceattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageSubjectAdapter extends RecyclerView.Adapter<ManageSubjectAdapter.ManageSubjectViewHolder>
{
    private ArrayList<Subject> Items;
    private Context context;

    public ManageSubjectAdapter(ArrayList<Subject> items, Context context) {
        Items = items;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ManageSubjectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admin_manage_subjects,parent,false);
        return new ManageSubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ManageSubjectViewHolder holder, int position)
    {
        holder. admin_view_subject_name.setText(Items.get(position).getSubjectName());
        holder.admin_view_subject_code.setText(Items.get(position).getSubjectCode());
        holder. admin_view_qual_name.setText(Items.get(position).getCourseFaculty());
        holder.admin_view_qual_code.setText(Items.get(position).getCourseCode());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseID=Items.get(position).getCourseID();
                String courseCode =Items.get(position).getCourseCode();
                String subjectID=Items.get(position).getSubjectID();
                String subjectName =Items.get(position).getSubjectName();
                String subjectCode =Items.get(position).getSubjectCode();
                CharSequence options[] = new CharSequence[]{"Add Time Table","Assign This subject to lecture"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Manage Subject");
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if(which == 0)
                        {

                            Intent intent = new Intent(context, AddTimeTable.class);
                            intent.putExtra("courseID",courseID);
                            intent.putExtra("courseCode",courseCode);
                            intent.putExtra("subjectID",subjectID);
                            intent.putExtra("subjectName",subjectName);
                            intent.putExtra("subjectCode",subjectCode);
                            v.getContext().startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(context, Lactures.class);
                            intent.putExtra("courseID",courseID);
                            intent.putExtra("courseCode",courseCode);
                            intent.putExtra("subjectID",subjectID);
                            intent.putExtra("subjectName",subjectName);
                            intent.putExtra("subjectCode",subjectCode);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
                builder.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public static class ManageSubjectViewHolder extends RecyclerView.ViewHolder
    {
        public TextView admin_view_subject_name,admin_view_subject_code,admin_view_qual_name,admin_view_qual_code;
        public RelativeLayout layout;
        public ManageSubjectViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            admin_view_subject_name = itemView.findViewById(R.id.admin_view_subject_name);
            admin_view_subject_code= itemView.findViewById(R.id.admin_view_subject_code);
            admin_view_qual_name= itemView.findViewById(R.id.admin_view_qual_name);
            admin_view_qual_code= itemView.findViewById(R.id.admin_view_qual_code);
            layout = itemView.findViewById(R.id.relative2);
        }
    }
}
