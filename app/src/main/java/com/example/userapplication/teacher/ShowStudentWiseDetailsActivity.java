package com.example.userapplication.teacher;

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

import androidx.appcompat.app.AppCompatActivity;

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

import cz.msebera.android.httpclient.Header;

public class ShowStudentWiseDetailsActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    TextView tvName, tvEmail, tvMobileNo, tvEnrollment,tvBranch,tvClass,tvSemester,tvAadharno,tvAddress, tvUsername;
    SharedPreferences preferences;
    String strEnrollment;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_wise_details);

        tvName = findViewById(R.id.tvShowStudentWiseDetailsName);
        tvMobileNo = findViewById(R.id.tvShowStudentWiseDetailsMobileNo);
        tvEmail = findViewById(R.id.tvShowStudentWiseDetailsEmail);
        tvEnrollment = findViewById(R.id.tvShowStudentWiseDetailsEnrollmentNo);
        tvBranch = findViewById(R.id.tvShowStudentWiseDetailsBranch);
        tvClass = findViewById(R.id.tvShowStudentWiseDetailsClass);
        tvSemester = findViewById(R.id.tvShowStudentWiseDetailsSemester);
        tvAadharno = findViewById(R.id.tvShowStudentWiseDetailsAadharno);
        tvUsername = findViewById(R.id.tvShowStudentWiseDetailsUsername);
        tvAddress = findViewById(R.id.tvShowStudentWiseDetailsAddress);
        ivProfilePhoto = findViewById(R.id.ivShowStudentWiseDetailsProfilePhoto);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        strEnrollment = getIntent().getStringExtra("enrollmentno");


    }


    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Student Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        showStudentDetails();
    }

    private void showStudentDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("enrollmentno",strEnrollment);

        client.post(Urls.showStudentWiseDetailsWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("showStudentWiseDetails");

                    for (int i=0;i< jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strid = jsonObject.getString("id");
                        String strimage = jsonObject.getString("image");
                        String strName = jsonObject.getString("name");
                        String strMobileno = jsonObject.getString("mobileno");
                        String strEnrollmentNo = jsonObject.getString("enrollmentno");
                        String strEmailid = jsonObject.getString("emailid");
                        String strBranch = jsonObject.getString("branch");
                        String strclass = jsonObject.getString("class");
                        String strSemester = jsonObject.getString("semester");
                        String strAadharno = jsonObject.getString("aadharno");
                        String strAddress = jsonObject.getString("address");
                        String strUsername = jsonObject.getString("username");

                        tvName.setText(strName);
                        tvMobileNo.setText(strMobileno);
                        tvEnrollment.setText(strEnrollmentNo);
                        tvEmail.setText(strEmailid);
                        tvBranch.setText(strBranch);
                        tvClass.setText(strclass);
                        tvSemester.setText(strSemester);
                        tvAadharno.setText(strAadharno);
                        tvAddress.setText(strAddress);
                        tvUsername.setText(strUsername);

                        Glide.with(ShowStudentWiseDetailsActivity.this)
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

                Toast.makeText(ShowStudentWiseDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}