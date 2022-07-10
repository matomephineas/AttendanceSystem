package com.example.faceattendancesystem.DataHelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.example.faceattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class PopUpDioloug extends AppCompatDialogFragment {
    private TextInputEditText courseName,courseCode,courseFaculty;
    private android.app.AlertDialog alertDialog;
    private String coursename,coursecode,coursefaculty;

    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.activity_add_course,null);
        courseCode = view.findViewById(R.id.courseCode);
        courseName = view.findViewById(R.id.courseName);
        courseFaculty = view.findViewById(R.id.courseFaculty);

        alertDialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage("Please wait, Processing...")
                .setCancelable(false)
                .build();

                 builder.setView(view)
                .setTitle("Add Course")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        registerCourse();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
        return builder.create();
    }
    private boolean validateFaculty()
    {
        coursefaculty = courseFaculty.getText().toString().trim().toUpperCase();
        if(coursefaculty.isEmpty())
        {
            courseFaculty.setError("filed must not be empty");
            courseFaculty.requestFocus();
            return false;
        }
        else
        {
            courseFaculty.setError(null);
            courseFaculty.requestFocus();
            return true;
        }
    }
    private boolean validateCode()
    {
        coursename = courseName.getText().toString().trim().toUpperCase();
        if(coursename.isEmpty())
        {
            courseCode.setError("filed must not be empty");
            courseCode.requestFocus();
            return false;
        }
        else
        {
            courseCode.setError(null);
            courseCode.requestFocus();
            return true;
        }
    }
    private boolean validateName()
    {
        coursecode =courseCode.getText().toString().trim().toUpperCase();
        if(coursecode.isEmpty())
        {
            courseName.setError("filed must not be empty");
            courseName.requestFocus();
            return false;
        }
        else
        {
            courseName.setError(null);
            courseName.requestFocus();
            return true;
        }
    }
    private void registerCourse()
    {
        if(!validateName() | !validateCode() | !validateFaculty())
        {
            return;
        }
        else
        {
            alertDialog.show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Course").push();
            String courseID=reference.getKey();
           final HashMap<String,Object> hashMap= new HashMap<>();

            hashMap.put("courseID",courseID);
            hashMap.put("courseName",coursename);
            hashMap.put("courseCode",coursecode);
            hashMap.put("courseFaculty",coursefaculty);

            reference.updateChildren(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                //Toast.makeText(getContext(), "Course Registered successfully", Toast.LENGTH_SHORT).show();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student View Course").child(courseID);
                                ref.updateChildren(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task)
                                            {
                                              if(task.isSuccessful())
                                              {
                                                  //Toast.makeText(, "Course Registered successfully", Toast.LENGTH_SHORT).show();
                                                  alertDialog.dismiss();
                                              }
                                              else
                                              {
                                                  //Toast.makeText(getContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                  alertDialog.dismiss();
                                              }
                                            }
                                        });


                            }
                            else
                            {
                                //Toast.makeText(getContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                    });
        }
    }


}
