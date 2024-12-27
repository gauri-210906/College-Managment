package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.example.userapplication.common.Urls;
import com.example.userapplication.common.VolleyMultipartRequest;
import com.example.userapplication.student.StudentHomeActivity;
import com.example.userapplication.student.StudentRegistrationActivity;
import com.example.userapplication.student.VerifyOTPActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddYearWiseStudentTaskActivity extends AppCompatActivity {

    EditText etDeadline, etSubject, etDescription;
    AppCompatButton acBtnUploadImage, acBtnSubmit;
    String strYear;
    ImageView ivTaskImage;
    ProgressDialog progressDialog;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filepath;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_year_wise_student_task);

        strYear = getIntent().getStringExtra("year");

        etDeadline = findViewById(R.id.etAddYearWiseStudentTaskDeadline);
        etSubject = findViewById(R.id.etAddYearWiseStudentTaskSubject);
        etDescription = findViewById(R.id.etAddYearWiseStudentTaskDescription);
        acBtnSubmit = findViewById(R.id.acbtnStudentAddFragmentSubmit);
        acBtnUploadImage = findViewById(R.id.acbtnAddYearWiseStudentTaskUploadImage);
        ivTaskImage = findViewById(R.id.ivAddYearWiseStudentTaskImage);

        acBtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        acBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etSubject.getText().toString().isEmpty()) {
                    etSubject.setError("Please enter subject");
                } else if (etDeadline.getText().toString().isEmpty()) {
                    etDeadline.setError("Please enter deadline");
                } else if (etDescription.getText().toString().isEmpty()) {
                    etDescription.setError("Please enter description");
                } else {

                    progressDialog = new ProgressDialog(AddYearWiseStudentTaskActivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Updating task");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                    insertTaskDetails();

                }

            }
        });

    }

    private void insertTaskDetails() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("year", strYear);
        params.put("taskdeadline", etDeadline);
        params.put("taskdescription", etDescription);
        params.put("subject", etSubject);

        client.post(Urls.insertStudentTaskWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            progressDialog.dismiss();
                            String status = response.getString("success");

                            if (status.equals("1")){

                                uploadTaskImage(bitmap,response.getInt("lastinsertedid"));

//                                AlertDialog.Builder ad = new AlertDialog.Builder(AddYearWiseStudentTaskActivity.this);
//
//                                LayoutInflater inflater = getLayoutInflater();
//                                View dialogView = inflater.inflate(R.layout.alert_dialogbox2, null);
//
//                                ad.setView(dialogView);
//
//                                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).create().show();

                                Toast.makeText(AddYearWiseStudentTaskActivity.this, "Added task Successfully", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(AddYearWiseStudentTaskActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }


                }
        );

    }

    private void uploadTaskImage(Bitmap bitmap, int lastinsertedid) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                Urls.uploadStudentTaskImageWebService,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        Intent i = new Intent(AddYearWiseStudentTaskActivity.this, TeacherHomeActivity.class);
                        startActivity(i);
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddYearWiseStudentTaskActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

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

        Volley.newRequestQueue(AddYearWiseStudentTaskActivity.this).add(volleyMultipartRequest);

    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();

    }


    private void showFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Task Image"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(resultCode, requestCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            filepath = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                ivTaskImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}