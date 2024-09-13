package com.example.userapplication;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyOTPActivity extends AppCompatActivity {

    TextView tvMobileNo, tvResendOTP;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    EditText etInput1,etInput2,etInput3,etInput4,etInput5,etInput6;
    private String strVerificationCode, strName, strMobileNo,strEnrollmentNumber, strEmail, strUsername, strPassword;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);

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
        strName =  getIntent().getStringExtra("name");
        strMobileNo =  getIntent().getStringExtra("mobileno");
        strEnrollmentNumber = getIntent().getStringExtra("enrollmentno");
        strEmail =  getIntent().getStringExtra("emailid");
        strUsername =  getIntent().getStringExtra("username");
        strPassword =  getIntent().getStringExtra("password");

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

                    Toast.makeText(VerifyOTPActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();

                }

                String codeOTP = etInput1.getText().toString() + etInput2.getText().toString() + etInput3.getText().toString() +
                        etInput4.getText().toString() + etInput5.getText().toString() + etInput6.getText().toString();

                if (strVerificationCode != null){
                    progressDialog = new ProgressDialog(VerifyOTPActivity.this);
                    progressDialog.setTitle("Verifying OTP");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strVerificationCode, codeOTP);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                userRegisterDetails();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS, VerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
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

    private void userRegisterDetails() {
        // client server comm
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name", strName);
        params.put("mobileno", strMobileNo);
        params.put("enrollmentno",strEnrollmentNumber);
        params.put("emailid", strEmail);
        params.put("username", strUsername);
        params.put("password", strPassword);

        client.post(Urls.registerUserWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String status = response.getString("success");
                            if (status.equals("1")){
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(VerifyOTPActivity.this, StudentLoginActivity.class);
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Already Data Present ", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(VerifyOTPActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }


                }
        );

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