package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class TeacherMyProfileActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    TextView tvName, tvEmail, tvMobileNo,tvBranch, tvExperience,tvEducation,tvUsername, tvPassword;
    AppCompatButton btnLogout, btnEditProfile, btnEditProfilePhoto;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    String strUsername,strPassword;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filepath;
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_my_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TeacherMyProfileActivity.this);
        strUsername = sharedPreferences.getString("username","");
        strPassword = sharedPreferences.getString("password","");


        tvName = findViewById(R.id.tvTeacherMyProfileName);
        tvEmail = findViewById(R.id.tvTeacherMyProfileEmail);
        ivProfilePhoto = findViewById(R.id.ivTeacherMyProfilePhoto);
        tvMobileNo = findViewById(R.id.tvTeacherMyProfileMobileNo);
        tvBranch = findViewById(R.id.tvTeacherMyProfileBranch);
        tvExperience = findViewById(R.id.tvTeacherMyProfileExperience);
        tvEducation = findViewById(R.id.tvTeacherMyProfileEducation);
        tvUsername = findViewById(R.id.tvTeacherMyProfileUsername);
        tvPassword = findViewById(R.id.tvTeacherMyProfilePassword);
        btnEditProfile = findViewById(R.id.btnTeacherMyProfileEditProfile);
        btnLogout = findViewById(R.id.btnTeacherMyProfileSignOut);
        btnEditProfilePhoto = findViewById(R.id.btnTeacherMyProfileEditProfilePhoto);

        btnEditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherMyProfileActivity.this, TeacherLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void showFileChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Photo"), PICK_IMAGE_REQUEST);

        Intent intent = new Intent(TeacherMyProfileActivity.this, ChangeTeacherProfilePhotoActivity.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  // to convert image to byte array
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        intent.putExtra("bitmap", byteArray);

        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            filepath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                ivProfilePhoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        progressDialog = new ProgressDialog(TeacherMyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetails();
    }

    private void getMyDetails() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",strUsername);

        client.post(Urls.teacherGetMyDetailsWebService,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("teacherGetMyDetails");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strid = jsonObject.getString("id");
                        String strimage  = jsonObject.getString("image");
                        String strname = jsonObject.getString("name");
                        String strmobileno = jsonObject.getString("mobileno");
                        String stremailid = jsonObject.getString("email");
                        String strBranch = jsonObject.getString("branch");
                        String strExperience = jsonObject.getString("experience");
                        String strEducation = jsonObject.getString("education");
                        String strusername = jsonObject.getString("username");
                        String strpassword = jsonObject.getString("password");

                        tvName.setText(strname);
                        tvMobileNo.setText(strmobileno);
                        tvEmail.setText(stremailid);
                        tvBranch.setText(strBranch);
                        tvExperience.setText(strExperience);
                        tvEducation.setText(strEducation);
                        tvUsername.setText(strusername);
                        tvPassword.setText(strpassword);

                        Glide.with(TeacherMyProfileActivity.this)
                                .load(Urls.webServiceAddress+"images/"+strimage)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .error(R.drawable.image_not_found)
                                .into(ivProfilePhoto);


                        btnEditProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(TeacherMyProfileActivity.this,TeacherUpdateProfileActivity.class);
                                i.putExtra("name",strname);
                                i.putExtra("mobileno",strmobileno);
                                i.putExtra("email",stremailid);
                                i.putExtra("branch",strBranch);
                                i.putExtra("experience",strExperience);
                                i.putExtra("education",strEducation);
                                i.putExtra("username",strUsername);
                                i.putExtra("password",strpassword);
                                startActivity(i);
                            }
                        });



                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                progressDialog.dismiss();
                Toast.makeText(TeacherMyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }


        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}