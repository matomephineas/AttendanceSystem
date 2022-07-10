package com.example.faceattendancesystem.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.DataHelper.Courses;
import com.example.faceattendancesystem.DataHelper.User;
import com.example.faceattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.viewHolder>
{
    private ArrayList<Courses> Items;
    private Context context;
    DatabaseReference retrieveUser;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String studentNumber,emailAddress;

    public CourseAdapter(ArrayList<Courses> items, Context context) {
        Items = items;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_course,parent,false);
        mAuth =FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        holder.coureName.setText("Name: "+Items.get(position).getCourseName());
        holder.courseCode.setText("Code: "+Items.get(position).getCourseCode());
        holder.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CharSequence options[] = new CharSequence[]{"Yes","No"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Register Course");
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        retrieveUser= FirebaseDatabase.getInstance().getReference("Users");
                        retrieveUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                User user= snapshot.getValue(User.class);
                                studentNumber = user.getStudentNumber();
                                emailAddress = user.getEmail();

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        if(which == 0)
                        {
                           DatabaseReference check = FirebaseDatabase.getInstance().getReference("Students Registered Course");
                           check.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                               {
                                    if(snapshot.equals(studentNumber) && snapshot.equals(Items.get(position).getCourseID()))
                                    {
                                        Toast.makeText(context, "You have already registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setTitle("Register faculty");
                                        progressDialog.setMessage("Registering Course");
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students Registered Course");
                                        HashMap<String,Object> hashMap = new HashMap<>();
                                        hashMap.put("studentNumber",studentNumber);
                                        hashMap.put("email",emailAddress);
                                        hashMap.put("registered","registered");
                                        hashMap.put("courseID",Items.get(position).getCourseID());
                                        hashMap.put("courseName",Items.get(position).getCourseName());
                                        hashMap.put("courseCode",Items.get(position).getCourseCode());


                                        ref.child(Items.get(position).getCourseID())
                                                .updateChildren(hashMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task)
                                                    {
                                                        if(task.isSuccessful())
                                                        {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(context, "Course Registered", Toast.LENGTH_SHORT).show();
                                                                holder.btnRegister.setVisibility(View.INVISIBLE);
                                                                progressDialog.dismiss();
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(context, "error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(context, "error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                    }
                               }

                               @Override
                               public void onCancelled(@NonNull @NotNull DatabaseError error) {

                               }
                           });


                        }
                        else
                        {
                            Toast.makeText(context, "course not registered", Toast.LENGTH_SHORT).show();
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

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView coureName,courseCode;
        public Button btnRegister;
        public RelativeLayout layout;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            coureName = itemView.findViewById(R.id.view_course_name);
            courseCode = itemView.findViewById(R.id.view_course_code);
            btnRegister = itemView.findViewById(R.id.btnRegisterCourse);
        }
    }
}
