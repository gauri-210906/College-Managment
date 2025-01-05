package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterDepartmentWiseYear;
import com.example.userapplication.common.AdapterGetAllNotification;
import com.example.userapplication.common.POJODepartmentWiseYearList;
import com.example.userapplication.common.POJOGetAllNotification;
import com.example.userapplication.common.Urls;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddNotificationActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ListView lvNotesList;
    String strYear;

    List<POJOGetAllNotification> pojoGetAllNotifications;
    AdapterGetAllNotification adapterGetAllNotification;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_notification);

        strYear = getIntent().getStringExtra("year");

        lvNotesList = findViewById(R.id.lvAddNotificationFragmentListView);

        pojoGetAllNotifications = new ArrayList<>();

        getAllNotification();


        floatingActionButton = findViewById(R.id.addNotificationActivityFloatingButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNotificationActivity.this, AddNoteActivity.class);
                i.putExtra("year", strYear);
                startActivity(i);

            }
        });


    }

    private void getAllNotification() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.showAllNotificationTeacherWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getAllNotification");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strID = jsonObject.getString("id");
                        String strYear = jsonObject.getString("year");
                        String strTitle = jsonObject.getString("title");

                        pojoGetAllNotifications.add(new POJOGetAllNotification(strID, strYear, strTitle));
                    }

                    adapterGetAllNotification = new AdapterGetAllNotification(pojoGetAllNotifications, AddNotificationActivity.this);

                    lvNotesList.setAdapter(adapterGetAllNotification);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(AddNotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }

        });


    }
}
