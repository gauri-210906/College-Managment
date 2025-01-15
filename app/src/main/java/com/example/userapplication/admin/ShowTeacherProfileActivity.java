package com.example.userapplication.admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.example.userapplication.teacher.ShowStudentWiseDetailsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ShowTeacherProfileActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    TextView tvName, tvEmail, tvMobileNo,tvBranch,tvExperience,tvEducation,tvAddress, tvUsername;
    SharedPreferences preferences;
    String strid;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_teacher_profile);

        tvName = findViewById(R.id.tvShowStaffWiseDetailsName);
        tvMobileNo = findViewById(R.id.tvShowStaffWiseDetailsMobileNo);
        tvEmail = findViewById(R.id.tvShowStaffWiseDetailsEmail);
        tvBranch = findViewById(R.id.tvShowStaffWiseDetailsBranch);
        tvExperience = findViewById(R.id.tvShowStaffWiseDetailsExperience);
        tvEducation = findViewById(R.id.tvShowStaffWiseDetailsEducation);
        tvUsername = findViewById(R.id.tvShowStaffWiseDetailsAddress);
        tvAddress = findViewById(R.id.tvShowStudentWiseDetailsUsername);
        ivProfilePhoto = findViewById(R.id.ivShowStaffWiseDetailsProfilePhoto);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        strid = getIntent().getStringExtra("id");



    }

    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Staff Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        showStudentDetails();
    }

    private void showStudentDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("id",strid);

        client.post(Urls.adminShowStaffWiseDetailsWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("showStaffWiseDetails");

                    for (int i=0;i< jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strid = jsonObject.getString("id");
                        String strimage = jsonObject.getString("image");
                        String strName = jsonObject.getString("name");
                        String strMobileno = jsonObject.getString("mobileno");
                        String strEmailid = jsonObject.getString("email");
                        String strBranch = jsonObject.getString("branch");
                        String strExperience = jsonObject.getString("experience");
                        String strEducation = jsonObject.getString("education");
                        String strAddress = jsonObject.getString("address");
                        String strUsername = jsonObject.getString("username");

                        tvName.setText(strName);
                        tvMobileNo.setText(strMobileno);
                        tvEmail.setText(strEmailid);
                        tvBranch.setText(strBranch);
                        tvExperience.setText(strExperience);
                        tvEducation.setText(strEducation);
                        tvAddress.setText(strAddress);
                        tvUsername.setText(strUsername);

                        Glide.with(ShowTeacherProfileActivity.this)
                                .load(Urls.webServiceAddress+"images/"+strimage)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .error(R.drawable.image_not_found)
                                .into(ivProfilePhoto);


                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                progressDialog.dismiss();

                Toast.makeText(ShowTeacherProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }


}