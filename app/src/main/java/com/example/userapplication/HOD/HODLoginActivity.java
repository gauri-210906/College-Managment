package com.example.userapplication.HOD;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.example.userapplication.admin.AdminHomeActivity;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.example.userapplication.teacher.ConfirmRegisterMobileNumberTeacherActivity;
import com.example.userapplication.teacher.TeacherHomeActivity;
import com.example.userapplication.teacher.TeacherLoginActivity;
import com.example.userapplication.teacher.TeacherRegistrationActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HODLoginActivity extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvLoginHere,tvForgetPassword,tvNewUser;
    EditText etUsername, etPassword;
    CheckBox cbShowHide;
    AppCompatButton btnLogin;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hodlogin);

        ivLogo = findViewById(R.id.ivLoginLogo);
        tvLoginHere = findViewById(R.id.tvLoginTitle);
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        cbShowHide = findViewById(R.id.cbLoginShowHidePassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);
        tvNewUser = findViewById(R.id.tvLoginNewUser);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HODLoginActivity.this);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("isLogin", false)){
            Intent i = new Intent(HODLoginActivity.this, HodHomeActivity.class);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please enter your username");
                }
                else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please enter your password");
                }
                else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("Your username should be greater than 8 character");
                }
                else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("Your password should be greater than 8 character");
                }

                else {
                    progressDialog = new ProgressDialog(HODLoginActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Login under process...");
                    progressDialog.show();
                    userLogin();
                }

            }
        });

        cbShowHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    /* changes pass to hide*/
                }
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HODLoginActivity.this, HodRegistrationActivity.class);
                startActivity(i);
            }
        });


        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HODLoginActivity.this, ConfirmRegisterMobileNumberTeacherActivity.class);
                startActivity(i);
            }
        });


    }

    private void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", etUsername.getText().toString());
        params.put("password", etPassword.getText().toString());

        client.post(Urls.hodLoginWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            progressDialog.dismiss();

                            String status = response.getString("success");
                            String strUserrole = response.getString("userrole");

                            if (status.equals("1") && strUserrole.equals("hod")){


                                Intent i = new Intent(HODLoginActivity.this, HodHomeActivity.class);

                                editor.putBoolean("isLogin",true).commit();

                                startActivity(i);
                                finish();
                            } else if (status.equals("1") && strUserrole.equals("admin")){


                                Intent i = new Intent(HODLoginActivity.this, AdminHomeActivity.class);

                                editor.putBoolean("isLogin",true).commit();

                                startActivity(i);
                                finish();
                            }

                            else {
                                Toast.makeText(HODLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(HODLoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
}