package com.example.faceattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;


import com.example.faceattendancesystem.Adapter.TimeTableAdapter;
import com.example.faceattendancesystem.Adapter.WeekAdapter;
import com.example.faceattendancesystem.DataHelper.TimeTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class ViewTimeTable extends AppCompatActivity {
    private Toolbar toolbar;
    private TimeTableAdapter adapter;
    private ArrayList<TimeTable> mList;
    private RecyclerView recyclerView;
    private DatabaseReference retrieve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_time_table);
        toolbar = findViewById(R.id.toolbarTimeTableDetail);
        intiToolbar();
        setUpUIViews();
    }
    private void intiToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(WeekAdapter.sharedPreferences.getString(WeekAdapter.SET_DAY,null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setUpUIViews()
    {
        recyclerView =findViewById(R.id.rec_time_table);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        adapter = new TimeTableAdapter(mList,this);
        recyclerView.setAdapter(adapter);
        retrieve = FirebaseDatabase.getInstance().getReference("TimeTable").child(WeekAdapter.sharedPreferences.getString(WeekAdapter.SET_DAY,null));
        retrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    TimeTable timeTable = dataSnapshot.getValue(TimeTable.class);
                    mList.add(timeTable);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }
}