package com.example.userapplication;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.userapplication.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TeacherUpdateProfileActivity extends AppCompatActivity {

    EditText etName, etMobileno, etEmailID,etBranch,etExperience, etEducation, etUsername, etPassword;
    AppCompatButton btnEditProfile;
    String strName, strMobileno, strEmail, strBranch,strExperience, strEducation, strUsername, strPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_update_profile);

        etName = findViewById(R.id.etTeacherUpdateProfileName);
        etMobileno = findViewById(R.id.etTeacherUpdateProfilemobileno);
        etEmailID = findViewById(R.id.etTeacherUpdateProfileEmail);
        etBranch = findViewById(R.id.etTeacherUpdateProfileBranch);
        etExperience = findViewById(R.id.etTeacherUpdateProfileExperience);
        etEducation = findViewById(R.id.etTeacherUpdateProfileEducation);
        etUsername = findViewById(R.id.etTeacherUpdateProfileUsername);
        etPassword = findViewById(R.id.etTeacherUpdateProfilePassword);


        btnEditProfile = findViewById(R.id.btnTeacherUpdateProfileButton);


        strName = getIntent().getStringExtra("name");
        strMobileno = getIntent().getStringExtra("mobileno");
        strEmail = getIntent().getStringExtra("email");
        strBranch = getIntent().getStringExtra("branch");
        strExperience = getIntent().getStringExtra("experience");
        strEducation = getIntent().getStringExtra("education");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");

        etName.setText(strName);
        etMobileno.setText(strMobileno);
        etEmailID.setText(strEmail);
        etBranch.setText(strBranch);
        etExperience.setText(strExperience);
        etEducation.setText(strEducation);
        etUsername.setText(strUsername);
        etPassword.setText(strPassword);

        btnEditProfile .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(TeacherUpdateProfileActivity.this);
                progressDialog.setTitle("Updating Profile");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                teacherUpdateProfile();
            }
        });

    }


    private void teacherUpdateProfile() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",etName.getText().toString());
        params.put("mobileno",etMobileno.getText().toString());
        params.put("email",etEmailID.getText().toString());
        params.put("branch",etBranch.getText().toString());
        params.put("experience",etExperience.getText().toString());
        params.put("education",etEducation.getText().toString());
        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.updateProfileTeacherWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    String status = response.getString("success");

                    if (status.equals("1")){
                        Toast.makeText(TeacherUpdateProfileActivity.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TeacherUpdateProfileActivity.this,TeacherMyProfileActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(TeacherUpdateProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TeacherUpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}