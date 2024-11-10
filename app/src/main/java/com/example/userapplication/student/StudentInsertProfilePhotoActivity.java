package com.example.userapplication.student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.userapplication.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StudentInsertProfilePhotoActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    AppCompatButton btnAddProfilePhoto, btnNext;
    String strName,strMobileNo,strUsername,strPassword,strEmail, strEnrollmentNumber,strBranch,strClass, strSemester,strAadharno;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filepath;
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_insert_profile_photo);

        ivProfilePhoto = findViewById(R.id.ivStudentInsertProfilePhotoImage);
        btnAddProfilePhoto = findViewById(R.id.btnStudentInsertProfilePhotoImage);
        btnNext = findViewById(R.id.btnStudentInsertProfilePhotoNext);

        strName = getIntent().getStringExtra("name");
        strMobileNo = getIntent().getStringExtra("mobileno");
        strEmail = getIntent().getStringExtra("emailid");
        strEnrollmentNumber = getIntent().getStringExtra("enrollmentno");
        strBranch = getIntent().getStringExtra("branch");
        strClass = getIntent().getStringExtra("class");
        strSemester = getIntent().getStringExtra("semester");
        strAadharno = getIntent().getStringExtra("aadharno");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");


        btnAddProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(StudentInsertProfilePhotoActivity.this);
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Setting Your Profile Photo");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS,
                        StudentInsertProfilePhotoActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(StudentInsertProfilePhotoActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(StudentInsertProfilePhotoActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Intent i = new Intent(StudentInsertProfilePhotoActivity.this, VerifyOTPActivity.class);
                                i.putExtra("verificationCode", verificationCode);
                                i.putExtra("name", strName);
                                i.putExtra("mobileno",strMobileNo);
                                i.putExtra("enrollmentno",strEnrollmentNumber);
                                i.putExtra("emailid",strEmail);
                                i.putExtra("branch",strBranch);
                                i.putExtra("class",strClass);
                                i.putExtra("semester",strSemester);
                                i.putExtra("aadharno",strAadharno);
                                i.putExtra("username",strUsername);
                                i.putExtra("password",strPassword);

                                // convert bitmap into byte array
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                i.putExtra("bitmap", byteArray);
                                startActivity(i);


                            }
                        });

            }
        });


    }

    private void showFileChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Photo"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            filepath = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                ivProfilePhoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}