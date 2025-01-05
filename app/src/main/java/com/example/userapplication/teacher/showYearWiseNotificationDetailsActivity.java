package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterGetAllNotification;
import com.example.userapplication.common.POJOGetAllNotification;
import com.example.userapplication.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class showYearWiseNotificationDetailsActivity extends AppCompatActivity {

    ImageView  ivEdit;
    TextView tvTitle,tvDescription, tvyear;
    String strId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_year_wise_notification_details);


        strId = getIntent().getStringExtra("id");


        tvDescription = findViewById(R.id.tvShowYearWiseNotificationDetailsDescription);
        tvTitle = findViewById(R.id.tvShowYearWiseNotificationDetailsTitle);
        tvyear = findViewById(R.id.ivShowYearWiseNotificationDetailsYear);
        ivEdit = findViewById(R.id.ivShowYearWiseNotificationDetailsEditNote);

        getIdWiseNotification();


        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(showYearWiseNotificationDetailsActivity.this, UpdateNotificationActivity.class);
                i.putExtra("title",tvTitle.getText().toString());
                i.putExtra("id",strId);
                i.putExtra("description",tvDescription.getText().toString());
                startActivity(i);

            }
        });



    }



    private void getIdWiseNotification() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("id",strId);

        client.post(Urls.showIDWiseNotificationTeacherWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getIdWiseNotification");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strID = jsonObject.getString("id");
                        String strYear = jsonObject.getString("year");
                        String strTitle = jsonObject.getString("title");
                        String strDescription = jsonObject.getString("description");

                        tvTitle.setText(strTitle);
                        tvDescription.setText(strDescription);
                        tvyear.setText(strYear);


                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(showYearWiseNotificationDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }

        });



    }
}