package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.example.userapplication.common.Urls;
import com.example.userapplication.student.StudentMyProfileActivity;
import com.example.userapplication.student.UpdateStudentMyProfileActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateNotificationActivity extends AppCompatActivity {

   EditText etTitle, etDescription;
   Button btnUpdate;
   ProgressDialog progressDialog;
   String strTitle, strDescription,strID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_notification);

        etDescription = findViewById(R.id.etUpdateNotificationActivityDescription);
        etTitle = findViewById(R.id.etUpdateNotificationActivityTitle);
        btnUpdate = findViewById(R.id.btnUpdateNotificationActivityButton);

        strTitle = getIntent().getStringExtra("title");
        strID = getIntent().getStringExtra("id");
        strDescription = getIntent().getStringExtra("description");

        etTitle.setText(strTitle);
        etDescription.setText(strDescription);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(UpdateNotificationActivity.this);
                progressDialog.setTitle("Updating Note");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                updateNote();

            }
        });



    }

    private void updateNote() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("title",etTitle.getText().toString());
        params.put("id",strID);
        params.put("description",etDescription.getText().toString());


        client.post(Urls.updateNotificationTeacherWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    String status = response.getString("success");

                    if (status.equals("1")){
                        Toast.makeText(UpdateNotificationActivity.this, "Note Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateNotificationActivity.this, AddNotificationActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(UpdateNotificationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(UpdateNotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
