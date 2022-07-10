package com.example.faceattendancesystem.Adapter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.DataHelper.TimeTable;
import com.example.faceattendancesystem.DataHelper.User;
import com.example.faceattendancesystem.MarkRegister;
import com.example.faceattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {
    private ArrayList<TimeTable> Items;
    private Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String studentNumber;
    public static SharedPreferences sharedPreferences,sharedPreferences2,sharedPreferences3,sharedPreferences4,sharedPreferences5;
    public static String SET_NAME,DAY,CODE,ID;
    public static String SET_STARTTIME;

    public TimeTableAdapter(ArrayList<TimeTable> items, Context context) {
        Items = items;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_time_table,parent,false);
        sharedPreferences2= context.getSharedPreferences("NAME",Context.MODE_PRIVATE);
        sharedPreferences3= context.getSharedPreferences("DAY",Context.MODE_PRIVATE);
        sharedPreferences4= context.getSharedPreferences("CODE",Context.MODE_PRIVATE);
        sharedPreferences5= context.getSharedPreferences("ID",Context.MODE_PRIVATE);
        sharedPreferences= context.getSharedPreferences("TIME",Context.MODE_PRIVATE);
        notificationChannel();
        mAuth =FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.SubjectName.setText(Items.get(position).getSubjName());
        holder.SubjectVanue.setText("Code: "+Items.get(position).getSubjCode());
        holder.SubjectTime.setText("Time: "+Items.get(position).getStartTime()+" - "+ Items.get(position).getEndTime());

        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
//                studentNumber = user.getStudentNumber();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MarkRegister.class);
                sharedPreferences5.edit().putString(ID, Items.get(position).getSubjCode()).apply();
                sharedPreferences4.edit().putString(CODE, Items.get(position).getSubjCode()).apply();
                sharedPreferences3.edit().putString(DAY, Items.get(position).getSubjDay()).apply();
                sharedPreferences2.edit().putString(SET_NAME, Items.get(position).getSubjName()).apply();
                sharedPreferences.edit().putString(SET_STARTTIME,Items.get(position).getStartTime()).apply();
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return Items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView SubjectName,SubjectVanue,SubjectTime;
        public ImageView userimage;
        public RelativeLayout layout;
        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            SubjectName = itemView.findViewById(R.id.tvSubjectName);
            SubjectVanue = itemView.findViewById(R.id.tvSubjectVanue);
            SubjectTime = itemView.findViewById(R.id.tvSubjectTime);
            userimage = itemView.findViewById(R.id.accnt_image);
            layout = itemView.findViewById(R.id.ttlayout);
        }
    }
    private void notificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
