package com.example.faceattendancesystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattendancesystem.Adapter.CourseAdapter;
import com.example.faceattendancesystem.Adapter.userAdapter;
import com.example.faceattendancesystem.DataHelper.Courses;
import com.example.faceattendancesystem.DataHelper.User;
import com.example.faceattendancesystem.R;
import com.example.faceattendancesystem.ViewCourse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Lactures extends AppCompatActivity {

    private DatabaseReference retrieve;
    private userAdapter adapter;
    private ArrayList<User> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lactures);

        RecyclerView recyclerView = findViewById(R.id.lectures);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        adapter = new userAdapter(mList, this);
        recyclerView.setAdapter(adapter);
        retrieve = FirebaseDatabase.getInstance().getReference("Lectures");
        retrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                mList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    User course = dataSnapshot.getValue(User.class);
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