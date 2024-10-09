package com.example.userapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.common.NetworkChangeListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmRegisterMobileNumberTeacherActivity extends AppCompatActivity {

    EditText etConfirmRegisterMobileNo;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_register_mobile_number);


        etConfirmRegisterMobileNo = findViewById(R.id.etConfirmRegisterMobileNumberNumber);
        btnVerify = findViewById(R.id.btnConfirmRegisterNumberVerify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etConfirmRegisterMobileNo.getText().toString().isEmpty()){
                    etConfirmRegisterMobileNo.setError("Please enter mobile number");
                } else if (etConfirmRegisterMobileNo.getText().toString().length() != 10) {
                    etConfirmRegisterMobileNo.setError("Please enter valid mobile number");
                } else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etConfirmRegisterMobileNo.getText().toString(),
                            60, TimeUnit.SECONDS, ConfirmRegisterMobileNumberTeacherActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterMobileNumberTeacherActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterMobileNumberTeacherActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    Intent i = new Intent(ConfirmRegisterMobileNumberTeacherActivity.this, ForgetPasswordVerifyTecherActivity.class);

                                    i.putExtra("verificationCode", verificationCode);
                                    i.putExtra("mobileno",etConfirmRegisterMobileNo.getText().toString());

                                    startActivity(i);
                                    finish();
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