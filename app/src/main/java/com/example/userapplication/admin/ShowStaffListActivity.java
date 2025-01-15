package com.example.userapplication.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterGetAllStaffList;
import com.example.userapplication.common.AdapterGetYearWiseStudentsList;
import com.example.userapplication.common.POJOGetAllStaffList;
import com.example.userapplication.common.POJOGetYearWiseStudentsList;
import com.example.userapplication.common.Urls;
import com.example.userapplication.teacher.YearWiseStudentsListActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShowStaffListActivity extends AppCompatActivity {

    androidx.appcompat.widget.SearchView svSearchStudents;
    TextView tvNoStudentsAvailable;
    ListView lvStaffList;

    List<POJOGetAllStaffList> pojoGetAllStaffLists;
    AdapterGetAllStaffList adapterGetAllStaffList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_staff_list);

        svSearchStudents = findViewById(R.id.svStaffListSearchView);
        tvNoStudentsAvailable = findViewById(R.id.tvStaffListNoDataAvailable);
        lvStaffList = findViewById(R.id.lvStaffList);

        pojoGetAllStaffLists = new ArrayList<>();


        getStaffList(); // method

        svSearchStudents.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStaffByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchStaffByName(query);
                return false;
            }
        });

    }

    private void searchStaffByName(String query) {
        List<POJOGetAllStaffList> tempList = new ArrayList<>();
        tempList.clear();

        for (POJOGetAllStaffList obj : pojoGetAllStaffLists) {
            if (obj.getStrName().toUpperCase().contains(query.toUpperCase())) {

                tempList.add(obj);
            }
        }

        adapterGetAllStaffList = new AdapterGetAllStaffList(tempList, this);
        lvStaffList.setAdapter(adapterGetAllStaffList);

    }

    private void getStaffList() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.adminShowStaffListWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getStaffList");

                    if (jsonArray.isNull(0)) {
                        lvStaffList.setVisibility(View.GONE);
                        tvNoStudentsAvailable.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strid = jsonObject.getString("id");
                        String strName = jsonObject.getString("name");
                        String strBranch = jsonObject.getString("branch");


                        pojoGetAllStaffLists.add(new POJOGetAllStaffList(strid, strName, strBranch));
                    }
                    adapterGetAllStaffList = new AdapterGetAllStaffList(pojoGetAllStaffLists, ShowStaffListActivity.this);

                    lvStaffList.setAdapter(adapterGetAllStaffList);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ShowStaffListActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}


