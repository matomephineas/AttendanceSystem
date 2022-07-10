package com.example.faceattendancesystem.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.faceattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddSubject extends AppCompatActivity {

    TextView courseName,courseCode,faculty;
    EditText moduleName,moduleCode;
    String coursename,coursecode,courseid,moduleN,moduleC,courseFaculty;
    Button btnSubmit;
    ProgressDialog progressDialog;
    DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        Bundle bundle = getIntent().getExtras();
        coursename =bundle.getString("courseName");
        coursecode =bundle.getString("courseCode");
        courseid = bundle.getString("courseID");
        courseFaculty = bundle.getString("courseFaculty");

        courseName = findViewById(R.id.textCourseName);
        courseCode = findViewById(R.id.textCourseCode);
        faculty = findViewById(R.id.textCourseFaculty);

        moduleName = findViewById(R.id.moduleName);
        moduleCode = findViewById(R.id.moduleCode);
        btnSubmit = findViewById(R.id.btnSaveModule);
        progressDialog= new ProgressDialog(this);

        courseName.setText(coursename);
        courseCode.setText(coursecode);
        faculty.setText(courseFaculty);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AddCourseModule();
            }
        });
    }
    private boolean validateModuleName()
    {
        moduleN =moduleName.getText().toString().trim().toUpperCase();
        if(moduleN.isEmpty())
        {
            moduleName.setError("filed must not be empty");
            moduleName.requestFocus();
            return false;
        }
        else
        {
            moduleName.setError(null);
            moduleName.requestFocus();
            return true;
        }
    }
    private boolean validateModuleCode()
    {
        moduleC =moduleCode.getText().toString().trim().toUpperCase();
        if(moduleC.isEmpty())
        {
            moduleCode.setError("filed must not be empty");
            moduleCode.requestFocus();
            return false;
        }
        else
        {
            moduleCode.setError(null);
            moduleCode.requestFocus();
            return true;
        }
    }
    private void AddCourseModule()
    {
        if(!validateModuleName() | !validateModuleCode())
        {
            return;
        }
        else
        {
            progressDialog.setTitle("Create module");
            progressDialog.setMessage("Please wait while we add a module");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            root = FirebaseDatabase.getInstance().getReference("CourseModules").push();
            String moduleID = root.getKey();

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("subjectID",moduleID);
            hashMap.put("courseID",courseid);
            hashMap.put("courseFaculty",courseFaculty);
            hashMap.put("courseCode",coursecode);
            hashMap.put("subjectCode",moduleC);
            hashMap.put("subjectName",moduleN);

            root.updateChildren(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AddSubject.this, "Module Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddSubject.this, ModulesFragment.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(AddSubject.this, "Error occured: \n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}