package com.example.faceattendancesystem.Admin;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.Adapter.AdminCourseAdapter;
import com.example.faceattendancesystem.DataHelper.Courses;
import com.example.faceattendancesystem.DataHelper.PopUpDioloug;
import com.example.faceattendancesystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CourseFragment extends Fragment {


    private RecyclerView recyclerCourses;
    private DatabaseReference retreive;
    private FloatingActionButton fab,fab1;
    private AdminCourseAdapter adapter;
    private ArrayList<Courses> mList;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        recyclerCourses=view.findViewById(R.id.recycler_courses);
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab2);


        recyclerCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCourses.setHasFixedSize(true);

        mList = new ArrayList<>();
        adapter = new AdminCourseAdapter(mList,getContext());
        recyclerCourses.setAdapter(adapter);

        retreive = FirebaseDatabase.getInstance().getReference("Course");
        retreive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Courses course = dataSnapshot.getValue(Courses.class);
                    mList.add(course);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseFragment();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddTeacher.class));
            }
        });
        return view;
    }

    private void courseFragment()
    {
        PopUpDioloug popUpDioloug = new PopUpDioloug();
        popUpDioloug.show(getActivity().getSupportFragmentManager(), "popup diolog");

    }


}