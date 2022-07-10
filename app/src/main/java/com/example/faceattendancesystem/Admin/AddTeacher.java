package com.example.faceattendancesystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.faceattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddTeacher extends AppCompatActivity {
    private TextInputLayout regEditName,regEditEmail, regEditIdentity,regEditContact,regEditPassword,regEditConfirmPassword;
    private TextView alrdyRegistered,userView;
    private Button btnCreateAcnt;
    private ProgressDialog progressDialog;

    boolean isValid = true;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;
    String name, identity,phone,email,password,confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        mAuth = FirebaseAuth.getInstance();
        initializeFields();
    }
    private void initializeFields()
    {
        regEditName = findViewById(R.id.reg_driver_Name);
        regEditEmail =findViewById(R.id.reg_driver_email_address);
        regEditContact =findViewById(R.id.reg_driver_phone);
        regEditPassword =findViewById(R.id.reg_driver_password);
        regEditConfirmPassword =findViewById(R.id.reg_driver_confirm_password);
        progressDialog = new ProgressDialog(this);
        alrdyRegistered =findViewById(R.id.txt_alrdy_registered);
        //progressDialog = new ProgressDialog(this);
        btnCreateAcnt = findViewById(R.id.btn_register_driver);

        alrdyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                startActivity(intent);
            }
        });
        btnCreateAcnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount()
    {
        name =regEditName.getEditText().getText().toString().trim();
        phone =regEditContact.getEditText().getText().toString().trim();
        email =regEditEmail.getEditText().getText().toString().trim();
        password =regEditPassword.getEditText().getText().toString().trim();
        confirmpassword =regEditConfirmPassword.getEditText().getText().toString().trim();

        if(name.isEmpty())
        {
            regEditName.setError("filed must not be empty");
            regEditName.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            regEditEmail.setError("filed must not be empty");
            regEditEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            regEditEmail.setError("email address is not valid");
            regEditEmail.requestFocus();
            return;
        }

        if(phone.isEmpty())
        {
            regEditContact.setError("filed must not be empty");
            regEditContact.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(phone).matches())
        {
            regEditContact.setError("Phone number is invalid");
            regEditContact.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            regEditPassword.setError("filed must not be empty");
            regEditPassword.requestFocus();
            return;
        }
        if(password.length() < 5)
        {
            regEditPassword.setError("password too small");
            regEditPassword.requestFocus();
            return;
        }
        if(confirmpassword.isEmpty())
        {
            regEditConfirmPassword.setError("filed must not be empty");
            regEditConfirmPassword.requestFocus();
            return;
        }
        if(!confirmpassword.equals(password))
        {
            regEditConfirmPassword.setError("password does not match");
            regEditConfirmPassword.requestFocus();
            return;
        }

        else
        {
            progressDialog.setTitle("Create new account");
            progressDialog.setMessage("Please wait while checking your credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                HashMap<String,Object> map = new HashMap<>();

                                DatabaseReference userRef =  FirebaseDatabase.getInstance().getReference("Lectures");

                                map.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                map.put("name",name);

                                map.put("phone",phone);
                                map.put("email",email);
                                map.put("password",password);
                                map.put("userType",3);


                                userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(), "Lecture Registration Successfull", Toast.LENGTH_LONG).show();
                                            //Intent intent = new Intent(getApplicationContext(), AdminViewCourse.class);
                                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            //startActivity(intent);

                                            progressDialog.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Error: \n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }

                                });
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error: \n" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

}