package com.example.faceattendancesystem.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

;

import com.example.faceattendancesystem.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GenerateTextFile extends AppCompatActivity {

    private EditText todoText;
    private Button saveButton,retrievData;
    private String email,studentnumber,date,subjectname;
    TextView txtEmail,stdntno,subject_name,datetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_text_file);
        Bundle bundle = getIntent().getExtras();
        email =bundle.getString("email");
        studentnumber =bundle.getString("studentnumber");
        date= bundle.getString("date");
        subjectname = bundle.getString("subjectname");
        saveButton = findViewById(R.id.saveButton);
        txtEmail = findViewById(R.id.txtEmail);
        stdntno = findViewById(R.id.stdntno);
        subject_name = findViewById(R.id.subject_name);
        datetime = findViewById(R.id.datetime);
        retrievData = findViewById(R.id.retrievData);

//
//        datetime.setText(date);
//        txtEmail.setText(email);
//        stdntno.setText(studentnumber);
//        subject_name.setText(subjectname);
        retrievData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    if(readFromFile() != null)
                    {
                        datetime.setText(readFromFile());
                        txtEmail.setText(readFromFile());
                        stdntno.setText(readFromFile());
                        subject_name.setText(readFromFile());

                        datetime.setVisibility(View.VISIBLE);
                        txtEmail.setVisibility(View.VISIBLE);
                        stdntno.setVisibility(View.VISIBLE);
                        subject_name.setVisibility(View.VISIBLE);
                    }
                } catch (IOException e) {

                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToFile(email,studentnumber,date,subjectname);
                Toast.makeText(getApplicationContext(), "Saved successfully to textfile!", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void writeToFile(String email, String studentnumber, String date, String subjectname)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("todolist.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(email);
            outputStreamWriter.write(studentnumber);
            outputStreamWriter.write(date);
            outputStreamWriter.write(subjectname);
            outputStreamWriter.write(subjectname);
            outputStreamWriter.close();

        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readFromFile() throws IOException {
        String result = "";
        InputStream inputStream = openFileInput("todolist.txt");
        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            StringBuilder stringBuilder = new StringBuilder();

            while((temp = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(temp);
                stringBuilder.append("\n");
            }

            inputStream.close();
            result = stringBuilder.toString();
        }
        return result;
    }
}