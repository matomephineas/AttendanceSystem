package com.example.faceattendancesystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import dmax.dialog.SpotsDialog;

public class RegistrationPage extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLARY_REQUEST_CODE = 105;
    private TextView nextPage;
    private ImageView regImageView;
    private Button btnOpenCamera,btnOpenGallery;
    private String studentNumber,saveCurrentTime,currentPhotoPath,name,email,password,downloadImageUrl,confirmPassword,productRandomKey, imgURL,saveCurrentDate;
    private TextInputLayout regEditName, regEditEmail,regEditPassword,regEditConfirmPassword;
    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;

    private StorageReference storageReference;
    int userType=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nextPage =findViewById(R.id.nextPage);
        btnOpenCamera=findViewById(R.id.btn_open_camera);
        regEditName = findViewById(R.id.reg_name);
        regImageView =findViewById(R.id.regIcon);
        regEditEmail = findViewById(R.id.reg_email);
        regEditPassword =findViewById(R.id.reg_password);
        regEditConfirmPassword = findViewById(R.id.reg_confirm_password);

        storageReference =FirebaseStorage.getInstance().getReference();

        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait, Processing...")
                .setCancelable(false)
                .build();

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(regImageView.getDrawable() == null)
               {
                   Toast.makeText(RegistrationPage.this, "Before proceeding capture an image", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   registerUser();
               }

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
    private boolean validateConfirmPassword()
    {
        confirmPassword =regEditConfirmPassword.getEditText().getText().toString().trim();
        if(confirmPassword.isEmpty())
        {
            regEditConfirmPassword.setError("filed must not be empty");
            regEditConfirmPassword.requestFocus();
            return false;
        }
        if(!confirmPassword.equals(password))
        {
            regEditConfirmPassword.setError("password does not match");
            regEditConfirmPassword.requestFocus();
            return false;
        }
        else
        {
            regEditConfirmPassword.setError(null);
            regEditConfirmPassword.requestFocus();
            return true;
        }

    }
    private void askCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else
        {
            openCamera();
        }
      
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERM_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openCamera();
            }
            else
            {
                Toast.makeText(this, "Camera permissions is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CAMERA_REQUEST_CODE)
        {

            if(resultCode == Activity.RESULT_OK)
            {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat CurrentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = CurrentDate.format(calendar.getTime());

                SimpleDateFormat CurrentTime = new SimpleDateFormat("HH:MM:SS a");
                saveCurrentTime = CurrentTime.format(calendar.getTime());

                 productRandomKey = saveCurrentDate + saveCurrentTime;

                File f =new File(currentPhotoPath);
                regImageView.setImageURI(Uri.fromFile(f));
                 imgURL= Uri.fromFile(f).toString();

               StorageReference filePath =storageReference.child("pictures/" + productRandomKey + ".jpg");
                final UploadTask uploadTask = filePath.putFile(Uri.fromFile(f));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @NonNull
                            @NotNull
                            @Override
                            public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception
                            {
                                if(!task.isSuccessful())
                                {
                                    throw task.getException();

                                }
                                downloadImageUrl = filePath.getDownloadUrl().toString();
                                return  filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                if(task.isSuccessful())
                                {
                                    downloadImageUrl = task.getResult().toString();

                                    registerUser();

                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e)
                    {
                        String message = e.toString();
                        Toast.makeText(RegistrationPage.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
            }
        }
        if(requestCode ==GALLARY_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri contentUri = data.getData();
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName ="JPEG_"+ timestamp+"."+getFileExt(contentUri);
                Log.d("tag","Absolute Uri of an image "+imageFileName);
                regImageView.setImageURI(contentUri);
            }

        }
    }
    private String getFileExt(Uri contentUri)
    {
        ContentResolver c=getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(c.getType(contentUri));
    }
    private File createImageFile() throws IOException
    {
        //create image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName ="JPEG_"+ timestamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       // File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image =File.createTempFile(
                imageFileName,//prefix
                ".jpg",//suffix
                storageDir//dir name
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void openCamera()
    {
        Intent takePictureIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there is camera activity to handle the intent
        if(takePictureIntent.resolveActivity(getPackageManager()) !=null)
        {
            //create a file where a picture should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException ex){

            }
            //continue if file was successfully created
            if(photoFile !=null)
            {
                Uri photoUri = FileProvider.getUriForFile(this,"com.example.attendance.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE);
            }

        }
    }
    private void registerUser()
    {
        if(!validateEmail() | !validatePassword() | !validateConfirmPassword())
        {
            return;
        }
        else
        {
            alertDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                Random r = new Random();

                                HashSet<Integer> set = new HashSet<Integer>();

                                while(set.size() <1)
                                {
                                    int random = r.nextInt(999999)+1000000;
                                    set.add(random);
                                }
                                for(int ra:set)
                                {
                                    //to generate student number
                                    DateFormat df = new SimpleDateFormat("yy");
                                    String fd = df.format(Calendar.getInstance().getTime());
                                    String sum = fd + ra;
                                    studentNumber = sum;
                                }

                                HashMap<String,Object> map = new HashMap<>();

                                DatabaseReference userRef =  FirebaseDatabase.getInstance().getReference("Users");
                                final String UserId =FirebaseAuth.getInstance().getCurrentUser().getUid();
                                map.put("userID",UserId);
                                map.put("email",email);
                                map.put("registrationDate",saveCurrentDate);
                                map.put("registrationTime",saveCurrentTime);
                                map.put("image",downloadImageUrl);
                                map.put("studentNumber",studentNumber);
                                map.put("password",password);
                                //map.put("name",name);
                                map.put("userType",userType);

                                userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(RegistrationPage.this, "Registration Successfull", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegistrationPage.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                            alertDialog.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(RegistrationPage.this, "Error: \n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }
                                    }

                                });
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegistrationPage.this, "Error: \n" + message, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                    });
        }
    }
}