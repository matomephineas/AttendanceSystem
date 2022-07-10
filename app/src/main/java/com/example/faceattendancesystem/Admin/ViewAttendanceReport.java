package com.example.faceattendancesystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.example.faceattendancesystem.Adapter.AdminReportAdapter;
import com.example.faceattendancesystem.DataHelper.AttendanceReport;
import com.example.faceattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewAttendanceReport extends AppCompatActivity {

    private DatabaseReference retrieve;
    private AdminReportAdapter adapter;
    private ArrayList<AttendanceReport> mList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_report);
        recyclerView =findViewById(R.id.report);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mList = new ArrayList<>();
        adapter = new AdminReportAdapter(mList, this);
        recyclerView.setAdapter(adapter);
        retrieve = FirebaseDatabase.getInstance().getReference("AdminViewReport");
        retrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                mList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    AttendanceReport course = dataSnapshot.getValue(AttendanceReport.class);
                    mList.add(course);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}