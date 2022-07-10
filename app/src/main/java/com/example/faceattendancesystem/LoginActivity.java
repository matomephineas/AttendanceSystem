package com.example.faceattendancesystem;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.faceattendancesystem.Admin.AdminMainActivity;
import com.example.faceattendancesystem.DataHelper.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView forgetPassword,createAccount;
    private TextInputLayout regEditEmail,regEditPassword;
    private String email,password;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private SpotsDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth =FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        btnLogin =findViewById(R.id.btnLogin);
        createAccount =findViewById(R.id.createAccount);
        forgetPassword =findViewById(R.id.forgotpassword);
        regEditEmail=findViewById(R.id.log_email);
        regEditPassword=findViewById(R.id.log_password);

        alertDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait, Processing...")
                .setCancelable(false)
                .build();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationPage.class);
                startActivity(intent);
            }
        });
    }
    private boolean validateEmail()
    {
        email =regEditEmail.getEditText().getText().toString().trim();
        if(email.isEmpty())
        {
            regEditEmail.setError("filed must not be empty");
            regEditEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            regEditEmail.setError("email address is not valid");
            regEditEmail.requestFocus();
            return false;
        }
        else
        {
            regEditEmail.setError(null);
            regEditEmail.requestFocus();
            return true;
        }
    }
    private boolean validatePassword()
    {
        password =regEditPassword.getEditText().getText().toString().trim();
        if(password.isEmpty())
        {
            regEditPassword.setError("filed must not be empty");
            regEditPassword.requestFocus();
            return false;
        }
        if(password.length() < 5)
        {
            regEditPassword.setError("password too small");
            regEditPassword.requestFocus();
            return false;
        }
        else
        {
            regEditPassword.setError(null);
            regEditPassword.requestFocus();
            return true;
        }
    }
    private void loginUser()
    {
        if(!validateEmail() | !validatePassword())
        {
            return;
        }
        else
        {
            alertDialog.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                String usertype =user.getUid();
                                checkUserType(usertype);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }

                        }
                    });
        }
    }

    private void checkUserType(String usertype)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(usertype);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    User userProfile = snapshot.getValue(User.class);
                    //assert userProfile != null;
                    int userType = (userProfile.getUserType());

                    switch (userType) {
                        case 1:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            alertDialog.dismiss();
                            break;
                        case 2:
                            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                            alertDialog.dismiss();
                            break;

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "User: "+ email +" does not exits", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
//        if(user !=null)
//        {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        }
    }
}