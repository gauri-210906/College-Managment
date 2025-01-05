package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddNoteActivity extends AppCompatActivity {

    EditText etTitle, etDescription;
    Button btnAdd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strYear;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);

        strYear = getIntent().getStringExtra("year");

        etTitle = findViewById(R.id.etAddNoteActivityTitle);
        etDescription = findViewById(R.id.etAddNoteActivityDescription);
        btnAdd = findViewById(R.id.btnAddNoteActivityButton);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddNoteActivity.this);
        editor = sharedPreferences.edit();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNotification();

            }
        });

    }

    private void addNotification() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("year",strYear);
        params.put("title", etTitle.getText().toString());
        params.put("description", etDescription.getText().toString());

        client.post(Urls.addNotificationtoStudentWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {

                            String status = response.getString("success");
                            if (status.equals("1")){
                                Toast.makeText(AddNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        Toast.makeText(AddNoteActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}