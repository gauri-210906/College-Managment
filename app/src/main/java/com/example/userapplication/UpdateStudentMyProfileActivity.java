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

public class UpdateStudentMyProfileActivity extends AppCompatActivity {

    EditText etName, etMobileno, etEnrollment, etEmailID,etBranch,etClass, etSemester,etAdharno, etUsername, etPassword;
    AppCompatButton btnEditProfile;
    String strName, strMobileno, strEnrollmentno, strEmail, strBranch,strClass, strSemester,strAdharno, strUsername, strPassword;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_student_my_profile);

        etName = findViewById(R.id.etUpdatestudentProfileName);
        etMobileno = findViewById(R.id.etUpdatestudentProfilemobileno);
        etEnrollment = findViewById(R.id.etUpdatestudentProfileEnrollment);
        etEmailID = findViewById(R.id.etUpdatestudentProfileEmail);
        etBranch = findViewById(R.id.etUpdatestudentProfileBranch);
        etClass = findViewById(R.id.etUpdatestudentProfileClass);
        etSemester = findViewById(R.id.etUpdatestudentProfileSemester);
        etUsername = findViewById(R.id.etUpdatestudentProfileUsername);
        etPassword = findViewById(R.id.etUpdatestudentProfilePassword);
        etAdharno = findViewById(R.id.etUpdatestudentProfileAadharno);

        btnEditProfile = findViewById(R.id.btnUpdatestudentProfileButton);


        strName = getIntent().getStringExtra("name");
        strMobileno = getIntent().getStringExtra("mobileno");
        strEnrollmentno = getIntent().getStringExtra("enrollmentno");
        strEmail = getIntent().getStringExtra("emailid");
        strBranch = getIntent().getStringExtra("branch");
        strClass = getIntent().getStringExtra("class");
        strSemester = getIntent().getStringExtra("semester");
        strAdharno = getIntent().getStringExtra("aadharno");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");

        etName.setText(strName);
        etMobileno.setText(strMobileno);
        etEmailID.setText(strEmail);
        etEnrollment.setText(strEnrollmentno);
        etBranch.setText(strBranch);
        etClass.setText(strClass);
        etSemester.setText(strSemester);
        etAdharno.setText(strAdharno);
        etUsername.setText(strUsername);
        etPassword.setText(strPassword);


        btnEditProfile .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UpdateStudentMyProfileActivity.this);
                progressDialog.setTitle("Updating Profile");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                updateProfile();
            }
        });

    }

    private void updateProfile() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",etName.getText().toString());
        params.put("mobileno",etMobileno.getText().toString());
        params.put("enrollmentno",etEnrollment.getText().toString());
        params.put("emailid",etEmailID.getText().toString());
        params.put("branch",etBranch.getText().toString());
        params.put("class",etClass.getText().toString());
        params.put("semester",etSemester.getText().toString());
        params.put("aadharno",etAdharno.getText().toString());
        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.updateProfileStudentWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    String status = response.getString("success");

                    if (status.equals("1")){
                        Toast.makeText(UpdateStudentMyProfileActivity.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateStudentMyProfileActivity.this,StudentMyProfileActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(UpdateStudentMyProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(UpdateStudentMyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}