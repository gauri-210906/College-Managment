package com.example.userapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class StudentMyProfileActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    TextView tvName, tvEmail, tvMobileNo, tvEnrollment, tvUsername, tvPassword;
    AppCompatButton btnLogout, btnEditProfile;
    GoogleSignInOptions googleSignInOptions; // shows gmail from your google account
    GoogleSignInClient googleSignInClient; // used to store selected mail option
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    String strUsername,strPassword;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_my_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StudentMyProfileActivity.this);
        strUsername = sharedPreferences.getString("username","");
        strPassword = sharedPreferences.getString("password","");


        tvName = findViewById(R.id.tvMyProfileName);
        tvEmail = findViewById(R.id.tvMyProfileEmail);
        ivProfilePhoto = findViewById(R.id.ivMyProfilePhoto);
        tvMobileNo = findViewById(R.id.tvMyProfileMobileNo);
        tvEnrollment = findViewById(R.id.tvMyProfileEnrollmentNo);
        tvUsername = findViewById(R.id.tvMyProfileUsername);
        tvPassword = findViewById(R.id.tvMyProfilePassword);
        btnEditProfile = findViewById(R.id.btnMyProfileEditProfile);
        btnLogout = findViewById(R.id.btnMyProfileSignOut);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(StudentMyProfileActivity.this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleSignInAccount != null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();

            tvName.setText(name);
            tvEmail.setText(email);


            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(StudentMyProfileActivity.this, StudentLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }



    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        progressDialog = new ProgressDialog(StudentMyProfileActivity.this);
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

        client.post(Urls.myDetailsWebService,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strid = jsonObject.getString("id");
                        String strimage  = jsonObject.getString("image");
                        String strname = jsonObject.getString("name");
                        String strmobileno = jsonObject.getString("mobileno");
                        String strenrollmentno = jsonObject.getString("enrollmentno");
                        String stremailid = jsonObject.getString("emailid");
                        String strusername = jsonObject.getString("username");
                        String strpassword = jsonObject.getString("password");

                        tvName.setText(strname);
                        tvMobileNo.setText(strmobileno);
                        tvEnrollment.setText(strenrollmentno);
                        tvEmail.setText(stremailid);
                        tvUsername.setText(strusername);
                        tvPassword.setText(strpassword);

                        Glide.with(StudentMyProfileActivity.this)
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
                Toast.makeText(StudentMyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }


        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}