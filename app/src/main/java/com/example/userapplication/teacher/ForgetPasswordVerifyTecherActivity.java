package com.example.userapplication.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgetPasswordVerifyTecherActivity extends AppCompatActivity {

    TextView tvMobileNo, tvResendOTP;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    EditText etInput1,etInput2,etInput3,etInput4,etInput5,etInput6;
    private String strVerificationCode, strMobileNo;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password_verify);

        tvMobileNo = findViewById(R.id.tvVerifyOTPMobileNo);
        tvResendOTP = findViewById(R.id.tvVerifyOTPResendOTP);
        etInput1 = findViewById(R.id.etVerifyOTPInput1);
        etInput2 = findViewById(R.id.etVerifyOTPInput2);
        etInput3 = findViewById(R.id.etVerifyOTPInput3);
        etInput4 = findViewById(R.id.etVerifyOTPInput4);
        etInput5 = findViewById(R.id.etVerifyOTPInput5);
        etInput6 = findViewById(R.id.etVerifyOTPInput6);
        btnVerify = findViewById(R.id.btnVerifyOTPVerify);

        strVerificationCode =  getIntent().getStringExtra("verificationCode");

        strMobileNo =  getIntent().getStringExtra("mobileno");


        tvMobileNo.setText(strMobileNo);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInput1.getText().toString().trim().isEmpty() ||
                        etInput2.getText().toString().trim().isEmpty() ||
                        etInput3.getText().toString().trim().isEmpty() ||
                        etInput4.getText().toString().trim().isEmpty() ||
                        etInput5.getText().toString().trim().isEmpty() ||
                        etInput6.getText().toString().trim().isEmpty()){

                    Toast.makeText(ForgetPasswordVerifyTecherActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();

                }

                String codeOTP = etInput1.getText().toString() + etInput2.getText().toString() + etInput3.getText().toString() +
                        etInput4.getText().toString() + etInput5.getText().toString() + etInput6.getText().toString();

                if (strVerificationCode != null){
                    progressDialog = new ProgressDialog(ForgetPasswordVerifyTecherActivity.this);
                    progressDialog.setTitle("Verifying OTP...");
                    progressDialog.setMessage("Please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strVerificationCode, codeOTP);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent i = new Intent(ForgetPasswordVerifyTecherActivity.this, SetUpnewPasswordTeacherActivity.class);
                                i.putExtra("mobileno",strMobileNo);
                                startActivity(i);
                                finish();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordVerifyTecherActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS, ForgetPasswordVerifyTecherActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordVerifyTecherActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordVerifyTecherActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                strVerificationCode = newVerificationCode;

                            }
                        });
            }
        });



        setupInputOTP();


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



    private void setupInputOTP() {

        etInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()){
                    etInput2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()){
                    etInput3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()){
                    etInput4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInput4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()){
                    etInput5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInput5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()){
                    etInput6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}