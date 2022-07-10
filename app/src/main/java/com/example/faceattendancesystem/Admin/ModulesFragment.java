package com.example.faceattendancesystem.Admin;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.Adapter.ManageSubjectAdapter;
import com.example.faceattendancesystem.DataHelper.Subject;
import com.example.faceattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ModulesFragment extends Fragment {

    private RecyclerView recyclerCourses;
    private ProgressDialog progressDialog;
    private String course_name,course_code,choice;
    private DatabaseReference ref,retreive;
    private ManageSubjectAdapter adapter;
    private ArrayList<Subject> mList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_module, container, false);
        recyclerCourses=view.findViewById(R.id.recycler_manage_modules);
        recyclerCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCourses.setHasFixedSize(true);

        mList = new ArrayList<>();
        adapter = new ManageSubjectAdapter(mList,getContext());
        recyclerCourses.setAdapter(adapter);
        retreive = FirebaseDatabase.getInstance().getReference("CourseModules");
        retreive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    try
                    {
                        Subject course = dataSnapshot.getValue(Subject.class);
                        mList.add(course);
                    }
                    catch (DatabaseException e)
                    {
                        Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return view;
    }
}