package com.example.faceattendancesystem.Admin;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.faceattendancesystem.DataHelper.NotifyService;
import com.example.faceattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddTimeTable extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String courseID,courseCode,subjectID,subjectName,subjectCode;
    private TextView tvTimer1,tvTimer2,textSubjectName,textSubjectCode;
    EditText tvDate,subjectVanue;
    int t1Hour,t1Minute,t2Hour,t2Minute;
    Button submit;
    ProgressDialog  progressDialog;
    DatabaseReference ref;
    String vanue,day,startTime,endTime;
    private List<String> mList;
    private String caseName,caseNote;
    Spinner spinner,spinner1;
    String dayType,dayTypeWeek;
    TextView tv,tv1;
    String[] bankNames={" ","Lab1","Lab2","Lab3"};
    String[] weeks={" ","Monday","Tuesday","Wednesday","Thursday","Friday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);
        tv = findViewById(R.id.tv);
        Bundle bundle = getIntent().getExtras();
        //get the data from previous page
        courseID = bundle.getString("courseID");
        courseCode= bundle.getString("courseCode");
        subjectID= bundle.getString("subjectID");
        subjectName = bundle.getString("subjectName");
        subjectCode= bundle.getString("subjectCode");
        //assign variables
        tvTimer1 = findViewById(R.id.subjectStartTime);
        tvTimer2 = findViewById(R.id.subjectEndTime);
        textSubjectName = findViewById(R.id.textSubjectName);
        textSubjectCode = findViewById(R.id.textSubjectCode);
        spinner =findViewById(R.id.spinner_spinner);
        spinner1 =findViewById(R.id.spinner_spinner1);

        tv1 = findViewById(R.id.tv2);
        createNotificationChannel();
        submit = findViewById(R.id.btnSaveTT);

        progressDialog = new ProgressDialog(this);

        textSubjectName.setText(subjectName);
        textSubjectCode.setText(subjectCode);

        tvTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTimeTable.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialize hour and minute
                                t1Hour = hourOfDay;
                                t1Minute = minute;
                                //Initialize calender
                                Calendar calendar = Calendar.getInstance();
                                //set hour and minute
                                calendar.set(0,0,0,t1Hour,t1Minute);
                                tvTimer1.setText(DateFormat.format("hh:mm",calendar));
                                startTime= tvTimer1.getText().toString();
                            }
                        },12,0,false
                );
                //display previous time selected
                timePickerDialog.updateTime(t1Hour,t1Minute);
                //show diolog
                timePickerDialog.show();

            }
        });
        tvTimer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTimeTable.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialize hour and minute
                                t2Hour = hourOfDay;
                                t2Minute = minute;
                                //Initialize calender
                                Calendar calendar = Calendar.getInstance();
                                //set hour and minute
                                calendar.set(0,0,0,t2Hour,t2Minute);
                                //set selected time on textview
                                tvTimer2.setText(DateFormat.format("hh:mm",calendar));
                                endTime = tvTimer2.getText().toString();
                            }
                        },12,0,false
                );
                //display previous time selected
                timePickerDialog.updateTime(t2Hour,t2Minute);
                //show diolog
                timePickerDialog.show();

            }
        });

        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayType = parent.getItemAtPosition(position).toString();
                if(position == 0)
                {
                    tv1.setText("Select day");
                }
                else
                    tv1.setText(dayType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);


        ArrayAdapter a = new ArrayAdapter(this,android.R.layout.simple_spinner_item,weeks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spinner1.setAdapter(a);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimeTable();
            }
        });
    }

    private void addTimeTable()
    {

        progressDialog.setTitle("Create module");
        progressDialog.setMessage("Please wait while we add a module");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        vanue = subjectVanue.getText().toString().trim();
        day = tvDate.getText().toString().trim();

        ref = FirebaseDatabase.getInstance().getReference("TimeTable").child(day).push();
        String key = ref.getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("courseID",courseID);
        hashMap.put("courseCode",courseCode);
        hashMap.put("subjID",subjectID);
        hashMap.put("subjName",subjectName);
        hashMap.put("subjCode",subjectCode);
        hashMap.put("timeTableID",key);
        hashMap.put("startTime",startTime);
        hashMap.put("endTime",endTime);
        hashMap.put("subjDay",dayTypeWeek);
        hashMap.put("subjVanue",dayType);

        ref.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            ref.child("AdminViewTimeTable").push()
                                    .updateChildren(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                         if(task.isSuccessful())
                                         {
                                             Toast.makeText(AddTimeTable.this, "Time table Added", Toast.LENGTH_SHORT).show();
                                             createBackNotifaction();
                                             progressDialog.dismiss();
                                         }
                                         else
                                         {
                                             Toast.makeText(AddTimeTable.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                         }
                                        }
                                    });

                            progressDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(AddTimeTable.this, "Error occured: \n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });


    }

    public void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1)
        {
            if(resultCode ==RESULT_OK && data !=null)
            {

            }
        }
    }


    private void createBackNotifaction()
    {
        Intent myIntent = new Intent(getApplicationContext() , NotifyService. class ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE ) ;
        PendingIntent pendingIntent = PendingIntent. getService ( this, 0 , myIntent , 0 ) ;
        Calendar calendar = Calendar. getInstance () ;
        calendar.set(Calendar. SECOND , 0 ) ;
        calendar.set(Calendar. MINUTE , 0 ) ;
        calendar.set(Calendar. HOUR , 0 ) ;
        calendar.set(Calendar. AM_PM , Calendar. AM ) ;
        calendar.add(Calendar. DAY_OF_MONTH , 1 ) ;
        alarmManager.setRepeating(AlarmManager. RTC_WAKEUP , calendar.getTimeInMillis() , 1000 * 60 * 60 * 24 , pendingIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dayType = parent.getItemAtPosition(position).toString();
        if(position == 0)
        {
           tv.setText("Select Vanue");
        }
        else
        tv.setText(dayType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}