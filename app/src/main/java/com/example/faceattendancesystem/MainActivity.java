package com.example.faceattendancesystem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.faceattendancesystem.Adapter.TimeTableAdapter;
import com.example.faceattendancesystem.DataHelper.TimeTable;
import com.example.faceattendancesystem.DataHelper.User;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RecyclerView recyclerView;
    private DatabaseReference retrieve;
    private TimeTableAdapter adapter;
    private ArrayList<TimeTable> mList;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String day;
    int hours;
    private CircleImageView imgAcnt;
    private ImageView dayImage;
   private TextView  user_name,user_email,studentNumber,today,courseName,goodMorning,tvToday;
    Calendar now =Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAcnt = findViewById(R.id.imUserProfile);
        tvToday = findViewById(R.id.textToday);
        dayImage = findViewById(R.id.dayIMG);
        user_name = findViewById(R.id.tvUsername);
        studentNumber = findViewById(R.id.tvStudentNumber);
        today = findViewById(R.id.tvDay);

        goodMorning = findViewById(R.id.tvMorning);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);


        Button button=findViewById(R.id.button);

        mAuth =FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        hours = c.get(Calendar.HOUR_OF_DAY);
        day = new SimpleDateFormat("EEEE").format(date);
        today.setText(day);
        tvToday.setText(day);
       //String value =day.substring(0,1);
        retrieve= FirebaseDatabase.getInstance().getReference("Users");
        retrieve.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                User user= snapshot.getValue(User.class);
                user_name.setText(user.getEmail());
                studentNumber.setText(user.getStudentNumber());
                Picasso.get().load(user.getImage()).into(imgAcnt);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        checkTime();
        setUpViews();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

     public void checkTime()
     {
         if(hours >=0 && hours <12)
         {
             goodMorning.setText("Good Morning");
             goodMorning.setVisibility(View.INVISIBLE);
             dayImage.setImageDrawable(getResources().getDrawable(R.drawable.goodmorning));
         }
         else if(hours >=12 && hours <16)
         {
             goodMorning.setText("Good Afternoon");
             goodMorning.setVisibility(View.INVISIBLE);
             dayImage.setImageDrawable(getResources().getDrawable(R.drawable.goodafternoon));
         }
         else if(hours >=16 && hours <21)
         {
             goodMorning.setText("Good Evening");
             goodMorning.setVisibility(View.INVISIBLE);
             dayImage.setImageDrawable(getResources().getDrawable(R.drawable.goodevening));
         }
         else if(hours >=21 && hours <24)
         {
             goodMorning.setText("Good Night");
             goodMorning.setVisibility(View.INVISIBLE);
             dayImage.setImageDrawable(getResources().getDrawable(R.drawable.night));
         }
     }
    void setUpViews()
    {
        recyclerView =findViewById(R.id.weeklyview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        adapter = new TimeTableAdapter(mList,this);
        recyclerView.setAdapter(adapter);

        //To retrieve subjects per day
        retrieve = FirebaseDatabase.getInstance().getReference("TimeTable").child(day);
        retrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    //it gets the data from the Time table class and puts them in an adapter
                    TimeTable timeTable = dataSnapshot.getValue(TimeTable.class);
                    mList.add(timeTable);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull @com.google.firebase.database.annotations.NotNull DatabaseError error) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item)
    {
        switch (item.getItemId()){

            case R.id.nav_add_subjects:
                Intent intent2 = new Intent(MainActivity.this,AddSubjects.class);
                startActivity(intent2);
                return true;

            case R.id.nav_add_course:
                Intent intent5 = new Intent(MainActivity.this,ViewCourse.class);
                startActivity(intent5);
                return true;

            case R.id.nav_add_attendance:
                Intent intent3 = new Intent(MainActivity.this,Attendance.class);
                startActivity(intent3);
                return true;

            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent4 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent4);
                return true;
        }
        return false;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
      now.set(Calendar.YEAR,year);
      now.set(Calendar.MONTH,month);
      now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
      tpd.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        dpd.show();


//        NotifyMe notifyMe=new NotifyMe.Builder(getApplicationContext())
//                .title("Class Time")
//                .content("Student please prepare for class")
//                .color(255,0,0,255)
//                .led_color(255,255,255,255)
//                .time(now)
//                .addAction(new Intent(),"snooze",false)
//                .key("test")
//                .addAction(new Intent(),"dismiss",true,false)
//                .addAction(new Intent(),"done")
//                .large_icon(R.drawable.notifications)
//                .build();


    }
}