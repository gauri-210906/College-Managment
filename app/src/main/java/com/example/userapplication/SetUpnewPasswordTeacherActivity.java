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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SetUpnewPasswordTeacherActivity extends AppCompatActivity {

    String strMobileNo;
    EditText etnewpassword, etconfirmnewpassword;
    AppCompatButton btnconfirm;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_upnew_password_teacher);


        strMobileNo = getIntent().getStringExtra("mobileno");

        etconfirmnewpassword = findViewById(R.id.etSetUpnewPasswordTeacherNewConfirmPassword);
        etnewpassword = findViewById(R.id.etSetUpnewPasswordTeacherNewPassword);
        btnconfirm = findViewById(R.id.btnSetUpnewPasswordTeacherConfirm);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etnewpassword.getText().toString().isEmpty() || etconfirmnewpassword.getText().toString().isEmpty()){
                    Toast.makeText(SetUpnewPasswordTeacherActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                else if(!etnewpassword.getText().toString().equals(etconfirmnewpassword.getText().toString())){
                    etconfirmnewpassword.setError("Password did not match");

                } else{
                    progressDialog = new ProgressDialog(SetUpnewPasswordTeacherActivity.this);
                    progressDialog.setTitle("Updating password");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    forgetPassword();
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

    private void forgetPassword() {

        AsyncHttpClient client = new AsyncHttpClient(); // client server communication
        RequestParams params = new RequestParams(); // put data

        params.put("mobileno",strMobileNo);
        params.put("password",etnewpassword.getText().toString());

        client.post(Urls.teacherForgetPasswordWebService, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        Intent i = new Intent(SetUpnewPasswordTeacherActivity.this, TeacherLoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SetUpnewPasswordTeacherActivity.this, "Password not changed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SetUpnewPasswordTeacherActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });



    }

}