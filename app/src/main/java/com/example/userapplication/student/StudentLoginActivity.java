package com.example.userapplication.student;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class StudentLoginActivity extends AppCompatActivity {

    ImageView ivLogo, ivGoogleSignIn;
    TextView tvLoginHere, tvNewUser,tvForgetPassword;
    EditText etUsername, etPassword;
    CheckBox cbShowHide;
    AppCompatButton btnLogin;
    ProgressDialog progressDialog;
    GoogleSignInOptions googleSignInOptions; // shows gmail from your google account
    GoogleSignInClient googleSignInClient; // used to store selected mail option
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        ivLogo = findViewById(R.id.ivLoginLogo);
        tvLoginHere = findViewById(R.id.tvLoginTitle);
        tvNewUser = findViewById(R.id.tvLoginNewUser);
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        cbShowHide = findViewById(R.id.cbLoginShowHidePassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        ivGoogleSignIn = findViewById(R.id.ivGoogle);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StudentLoginActivity.this);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("isLogin", false)){
            Intent i = new Intent(StudentLoginActivity.this, StudentHomeActivity.class);
            startActivity(i);
            finish();
        }

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(StudentLoginActivity.this, googleSignInOptions);

        ivGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });

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
                    progressDialog = new ProgressDialog(StudentLoginActivity.this);
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
                Intent i = new Intent(StudentLoginActivity.this, ConfirmRegisterMobileNumberActivity.class);
                startActivity(i);
            }
        });


        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentLoginActivity.this, StudentRegistrationActivity.class);
                startActivity(i);
            }
        });



    }


    private void signIn() {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent i = new Intent(StudentLoginActivity.this, StudentMyProfileActivity.class);
                startActivity(i);
                finish();

            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

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

                                Intent i = new Intent(StudentLoginActivity.this, StudentHomeActivity.class);
                                editor.putString("username",etUsername.getText().toString()).commit();
                                editor.putString("password", etPassword.getText().toString()).commit();

                                editor.putBoolean("isLogin",true).commit();

                                startActivity(i);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(StudentLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(StudentLoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}