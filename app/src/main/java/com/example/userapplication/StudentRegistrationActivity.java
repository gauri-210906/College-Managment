package com.example.userapplication;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class StudentRegistrationActivity extends AppCompatActivity {

    EditText etName,etMobileNo,etUsername,etPassword,etEmail, etEnrollmentNumber;
    Button btnRegister;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StudentRegistrationActivity.this);
        editor = sharedPreferences.edit();


        etName= findViewById(R.id.etRegisterName);
        etMobileNo= findViewById(R.id.etRegistermobileno);
        etEmail= findViewById(R.id.etRegisterEmail);
        etEnrollmentNumber = findViewById(R.id.etRegisterEnrollment);
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
                    etEmail.setError("Please enter email ID");
                } else if (!etEmail.getText().toString().contains("@") || !etEmail.getText().toString().contains(".com")) {
                    etEmail.setError("Invaild Email ID");
                }else if (etEnrollmentNumber.getText().toString().isEmpty()) {
                    etEnrollmentNumber.setError("Please enter your username");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please enter your username");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please enter your password");
                }
                else {

                    progressDialog = new ProgressDialog(StudentRegistrationActivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Registration is in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    // helps to verify mobile number
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etMobileNo.getText().toString(),
                            60, TimeUnit.SECONDS, StudentRegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(StudentRegistrationActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(StudentRegistrationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    Intent i = new Intent(StudentRegistrationActivity.this, VerifyOTPActivity.class);
                                    i.putExtra("verificationCode", verificationCode);
                                    i.putExtra("name",etName.getText().toString());
                                    i.putExtra("mobileno",etMobileNo.getText().toString());
                                    i.putExtra("enrollmentno",etEnrollmentNumber.getText().toString());
                                    i.putExtra("emailid",etEmail.getText().toString());
                                    i.putExtra("username",etUsername.getText().toString());
                                    i.putExtra("password",etPassword.getText().toString());

                                    startActivity(i);
                                }
                            });
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