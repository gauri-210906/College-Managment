package com.example.userapplication.HOD;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.example.userapplication.common.VolleyMultipartRequest;
import com.example.userapplication.teacher.TeacherHomeActivity;
import com.example.userapplication.teacher.TeacherVerifyOTPActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class HodVerifyActivity extends AppCompatActivity {

    TextView tvMobileNo, tvResendOTP;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    EditText etInput1,etInput2,etInput3,etInput4,etInput5,etInput6;
    private String strVerificationCode, strName, strMobileNo, strEmail, strBranch,strExperience,strEducation,strUsername, strPassword;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    double latitude, longitude;
    String address;
    Bitmap bitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hod_verify);

        if (ActivityCompat.checkSelfPermission(HodVerifyActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(HodVerifyActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(HodVerifyActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 101);

        }




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
        strEmail =  getIntent().getStringExtra("email");
        strBranch =  getIntent().getStringExtra("branch");
        strEducation =  getIntent().getStringExtra("experience");
        strExperience =  getIntent().getStringExtra("education");
        strUsername =  getIntent().getStringExtra("username");
        strPassword =  getIntent().getStringExtra("password");

        tvMobileNo.setText(strMobileNo);

        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");

        if (byteArray != null){
            bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);


        }

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInput1.getText().toString().trim().isEmpty() ||
                        etInput2.getText().toString().trim().isEmpty() ||
                        etInput3.getText().toString().trim().isEmpty() ||
                        etInput4.getText().toString().trim().isEmpty() ||
                        etInput5.getText().toString().trim().isEmpty() ||
                        etInput6.getText().toString().trim().isEmpty()){

                    Toast.makeText(HodVerifyActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();

                }

                String codeOTP = etInput1.getText().toString() + etInput2.getText().toString() + etInput3.getText().toString() +
                        etInput4.getText().toString() + etInput5.getText().toString() + etInput6.getText().toString();

                if (strVerificationCode != null){
                    progressDialog = new ProgressDialog(HodVerifyActivity.this);
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
                                getStudentCurrentLocation();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(HodVerifyActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS, HodVerifyActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(HodVerifyActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(HodVerifyActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("MissingPermission")
    private void getStudentCurrentLocation() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (HodVerifyActivity.this);

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                new CancellationToken() {
                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }


                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(HodVerifyActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                    address = addressList.get(0).getAddressLine(0);

                    teacherRegisterDetails(latitude,longitude,address);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HodVerifyActivity.this, ""+e, Toast.LENGTH_SHORT).show();

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

    private void teacherRegisterDetails(double latitude,double longitude, String address) {
        // client server comm
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name", strName);
        params.put("mobileno", strMobileNo);
        params.put("email", strEmail);
        params.put("branch", strBranch);
        params.put("experience", strExperience);
        params.put("education",strEducation);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("address",address);
        params.put("username", strUsername);
        params.put("password", strPassword);

        client.post(Urls.hodRegistrationWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            progressDialog.dismiss();
                            String status = response.getString("success");
                            if (status.equals("1")){

                                uploadProfilePhoto(bitmap,response.getInt("lastinsertedid"));

                            } else {

                                Toast.makeText(HodVerifyActivity.this, "Already Data Present ", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(HodVerifyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }


                }
        );

    }

    private void uploadProfilePhoto(Bitmap bitmap, int lastinsertedid) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                Urls.hodAddProfilePhotoWebService,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        Intent i = new Intent(HodVerifyActivity.this, HodHomeActivity.class);
                        startActivity(i);
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HodVerifyActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { // to send plain data through volley
                Map<String,String> params = new HashMap<>();
                params.put("tags",""+lastinsertedid);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError { // to send file or image through volley
                Map<String,VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic",new VolleyMultipartRequest.DataPart(imagename+".png",
                        getByteArrayFromBitmap(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(HodVerifyActivity.this).add(volleyMultipartRequest);

    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();

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