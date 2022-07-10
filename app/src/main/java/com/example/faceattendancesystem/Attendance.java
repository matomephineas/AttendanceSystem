package com.example.faceattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.faceattendancesystem.Adapter.AttendanceReportAdapter;
import com.example.faceattendancesystem.DataHelper.AttendanceReport;
import com.example.faceattendancesystem.DataHelper.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    private DatabaseReference retrieve,ref;
    private AttendanceReportAdapter adapter;
    private ArrayList<AttendanceReport> mList;
    private RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String studentNumber;
    private EditText inputText;
    private String searchInput;
    ImageView  searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        searchBtn =findViewById(R.id.searchBtn);
        inputText = findViewById(R.id.search_day);

        mAuth = FirebaseAuth.getInstance();
        user =mAuth.getCurrentUser();
        recyclerView =findViewById(R.id.rec_attendance_report);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();

        adapter = new AttendanceReportAdapter(mList, this);
        recyclerView.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchInput= inputText.getText().toString();
                onStart();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                studentNumber = user.getStudentNumber();
                retrieve = FirebaseDatabase.getInstance().getReference(studentNumber);
                retrieve.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        mList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            AttendanceReport at = dataSnapshot.getValue(AttendanceReport.class);
                            mList.add(at);

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


}