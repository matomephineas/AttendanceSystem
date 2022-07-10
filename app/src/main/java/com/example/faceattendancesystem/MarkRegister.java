package com.example.faceattendancesystem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.faceattendancesystem.Adapter.TimeTableAdapter;
import com.example.faceattendancesystem.DataHelper.GraphicOverlay;
import com.example.faceattendancesystem.DataHelper.RectOverlay;
import com.example.faceattendancesystem.DataHelper.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.squareup.picasso.Picasso;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;


import org.jetbrains.annotations.NotNull;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MarkRegister extends AppCompatActivity {
    private CameraView cameraView;
    TextView showOutput;
    ProgressBar progressBar;
    ImageView oriImage, testImage;
    FloatingActionButton btnCapture;
    private ImageView attResults;
    String strDate,strTime,day, studentName, studentNumber, studentEmail, originalResulsImage;
    private TextView subjName, studNum, startTime, TVRegisterResults, MarkedTime;
    private DatabaseReference retrieve;
    FirebaseAuth mAuth;
    FirebaseUser user;
    GraphicOverlay graphicOverlay;
    int id, id2, hours, min;
    private AlertDialog alertDialog;
    public static SharedPreferences pref1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_register);
        //initializing the hooks from the view page
        subjName = findViewById(R.id.sbjct_name);
        studNum = findViewById(R.id.stdnt_nmbr);
        startTime = findViewById(R.id.sbjct_attending_time);
        TVRegisterResults = findViewById(R.id.tv);
        attResults = findViewById(R.id.marked);
        MarkedTime = findViewById(R.id.marked_time);
        cameraView = findViewById(R.id.camera);
        showOutput = findViewById(R.id.output);
        progressBar = findViewById(R.id.progress_horizontal);
        btnCapture = findViewById(R.id.btn_mark_Attendance);
        oriImage = findViewById(R.id.original_image);
        graphicOverlay = findViewById(R.id.graphic_overlay);

        alertDialog = new SpotsDialog.Builder().setContext(this).setMessage("Please wait, Processing...").setCancelable(false).build();
        pref1 = getApplicationContext().getSharedPreferences("SET_STUDENT_NO", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        //gets the current logged in user
        user = mAuth.getCurrentUser();
        //initializing the dates
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        strDate = dateFormat.format(date);
        strTime = timeFormat.format(date);
        Calendar c = Calendar.getInstance();
        hours = c.get(Calendar.HOUR_OF_DAY);
        day = new SimpleDateFormat("EEEE").format(date);
        //Toast.makeText(this, "Time: "+ LocalTime.now().getHour(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, TimeTableAdapter.sharedPreferences.getString(TimeTableAdapter.SET_STARTTIME, null), Toast.LENGTH_SHORT).show();
        MarkedTime.setText(strTime);
        subjName.setText(TimeTableAdapter.sharedPreferences2.getString(TimeTableAdapter.SET_NAME, null));
        startTime.setText(TimeTableAdapter.sharedPreferences.getString(TimeTableAdapter.SET_STARTTIME, null));
        //Code to retrieve authenticated user from the database
        retrieve = FirebaseDatabase.getInstance().getReference("Users");
        retrieve.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                studentNumber = user.getStudentNumber();
                studNum.setText(studentNumber);
                studentName = user.getName();
                studentEmail = user.getEmail();
                Picasso.get().load(user.getImage()).into(oriImage);
                originalResulsImage = user.getImage();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        //Click listener for capturing an image
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });
        //Code to open camera
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }
            @Override
            public void onError(CameraKitError cameraKitError) {
            }
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                progressBar.setVisibility(View.VISIBLE);
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);

                Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.sarah);
                myBitmap = Bitmap.createScaledBitmap(myBitmap, cameraView.getWidth(), cameraView.getHeight(), false);


                cameraView.stop();
                processFaceDetection(bitmap,myBitmap);
            }
            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
            }
        });
    }
    //It process the captured image
    private void processFaceDetection(Bitmap bitmap,Bitmap myBitmap)
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions = new
                FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .setMinFaceSize(0.15f)
                .enableTracking()
                .build();
        FirebaseVisionFaceDetector firebaseVisionFaceDetector = FirebaseVision.getInstance()
                .getVisionFaceDetector(firebaseVisionFaceDetectorOptions);
        //checks if face is found
        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> Faces)
                    {
                        if(Faces.size() == 0)
                        {
                            Toast.makeText(MarkRegister.this, "Face not found", Toast.LENGTH_SHORT).show();
                        }
                        for(FirebaseVisionFace face: Faces)
                        {
                            Toast.makeText(MarkRegister.this, "Reached", Toast.LENGTH_SHORT).show();
                            RectOverlay rect =new RectOverlay(graphicOverlay,face.getBoundingBox());
                            graphicOverlay.add(rect);

                            if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID)
                            {
                                 id = face.getTrackingId();
                                //function to mark attentance if face is recognized
                                markAttendance();
                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    //function to mark attendance if a face is found
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void markAttendance()
    {
        alertDialog.show();
        //String markedTime = c.get(Calendar.HOUR_OF_DAY);
        TVRegisterResults.setText("Attendance:Marked");
        attResults.setImageResource(R.drawable.done);
        MarkedTime.setText(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(studentNumber).push();
        HashMap<String, Object> map = new HashMap<>();
        String key = ref.getKey();
        map.put("subjectName", TimeTableAdapter.sharedPreferences2.getString(TimeTableAdapter.SET_NAME, null));
        map.put("studentNo", studentNumber);
        map.put("attendanceID", key);
        map.put("studentEmail",studentEmail);
        map.put("attendanceStatus", "Attended");
        map.put("attendanceDate",strDate);
        map.put("attendanceTime", strTime);
        ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull @NotNull Task<Void> task)
             {
               if(task.isSuccessful())
               {
                   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AdminViewReport").push();
                   ref.updateChildren(map)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull @NotNull Task<Void> task) {
                                   if(task.isSuccessful())
                                   {
                                       Intent intent = new Intent(MarkRegister.this, MainActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       startActivity(intent);
                                       Toast.makeText(MarkRegister.this, "Attendace marked", Toast.LENGTH_SHORT).show();
                                      alertDialog.dismiss();
                                   }
                                   else
                                   {
                                       Toast.makeText(MarkRegister.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                                     alertDialog.dismiss();
                                   }
                               }
                           });
               }
             }
         });

    }
    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }
}