package com.example.userapplication;

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

import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TeacherLoginActivity extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvLoginHere,tvForgetPassword;
    EditText etUsername, etPassword;
    CheckBox cbShowHide;
    AppCompatButton btnLogin;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        ivLogo = findViewById(R.id.ivLoginLogo);
        tvLoginHere = findViewById(R.id.tvLoginTitle);
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        cbShowHide = findViewById(R.id.cbLoginShowHidePassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TeacherLoginActivity.this);
        editor = sharedPreferences.edit();

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
                    progressDialog = new ProgressDialog(TeacherLoginActivity.this);
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


        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherLoginActivity.this, ConfirmRegisterMobileNumberActivity.class);
                startActivity(i);
            }
        });


    }


    private void userLogin() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", etUsername.getText().toString());
        params.put("password", etPassword.getText().toString());

        client.post(Urls.loginUserWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String status = response.getString("success");
                            if (status.equals("1")){
                                progressDialog.dismiss();

                                Intent i = new Intent(TeacherLoginActivity.this, TeacherHomeActivity.class);
                                editor.putString("username",etUsername.getText().toString()).commit();
                                editor.putString("password", etPassword.getText().toString()).commit();
                                startActivity(i);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(TeacherLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(TeacherLoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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