package com.example.userapplication.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;

public class TeacherRegistrationActivity extends AppCompatActivity {

    EditText etName,etMobileNo,etUsername,etPassword,etEmail,etExperience,etEducation,etBranch;
    Button btnRegister;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_registration);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TeacherRegistrationActivity.this);
        editor = sharedPreferences.edit();


        etName= findViewById(R.id.etRegisterName);
        etMobileNo= findViewById(R.id.etRegistermobileno);
        etEmail= findViewById(R.id.etRegisterEmail);
        etEducation = findViewById(R.id.etRegisterEducation);
        etExperience = findViewById(R.id.etRegisterExperience);
        etBranch = findViewById(R.id.etRegisterBranch);
        etUsername= findViewById(R.id.etRegisterUsername);
        etPassword= findViewById(R.id.etRegisterPassword);
        btnRegister= findViewById(R.id.btnStudentRegistrationRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please enter your name");
                } else if (etMobileNo.getText().toString().isEmpty()) {
                    etMobileNo.setError("Please enter your mobile number");
                }  else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Please enter Email ID");
                } else if (etBranch.getText().toString().isEmpty()) {
                    etBranch.setError("Please enter branch ");
                } else if (etExperience.getText().toString().isEmpty()) {
                    etExperience.setError("Please enter year");
                } else if (etEducation.getText().toString().isEmpty()) {
                    etEducation.setError("Please enter semester");
                }  else if (!etEmail.getText().toString().contains("@") || !etEmail.getText().toString().contains(".com")) {
                    etEmail.setError("Invaild Email ID");
                }  else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please enter your username");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please enter your password");
                }
                else {

                    progressDialog = new ProgressDialog(TeacherRegistrationActivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Registration is in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                    Intent i = new Intent(TeacherRegistrationActivity.this, ChangeTeacherProfilePhotoActivity.class);
                    i.putExtra("name",etName.getText().toString());
                    i.putExtra("mobileno",etMobileNo.getText().toString());
                    i.putExtra("email",etEmail.getText().toString());
                    i.putExtra("branch",etBranch.getText().toString());
                    i.putExtra("experience", etExperience.getText().toString());
                    i.putExtra("education",etEducation.getText().toString());
                    i.putExtra("username",etUsername.getText().toString());
                    i.putExtra("password",etPassword.getText().toString());

                    startActivity(i);


                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }



}
