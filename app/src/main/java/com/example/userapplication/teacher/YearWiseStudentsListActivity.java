package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterGetYearWiseStudentsList;
import com.example.userapplication.common.POJOGetYearWiseStudentsList;
import com.example.userapplication.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class YearWiseStudentsListActivity extends AppCompatActivity {

    androidx.appcompat.widget.SearchView svSearchStudents;
    TextView tvNoStudentsAvailable;
    ListView lvStudentsList;
    String strClass;
    List<POJOGetYearWiseStudentsList> pojoGetYearWiseStudentsLists;
    AdapterGetYearWiseStudentsList adapterGetYearWiseStudentsList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_wise_students_list);

        svSearchStudents = findViewById(R.id.svYearWiseStudentsListSearchView);
        tvNoStudentsAvailable = findViewById(R.id.tvYearWiseStudentsListNoDataAvailable);
        lvStudentsList = findViewById(R.id.lvYearWiseStudentsList);

        pojoGetYearWiseStudentsLists = new ArrayList<>();

        strClass = getIntent().getStringExtra("class");

        getYearWiseStudentsList(); // method

        svSearchStudents.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStudentsByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchStudentsByName(query);
                return false;
            }
        });

    }

    private void searchStudentsByName(String query) {
        List<POJOGetYearWiseStudentsList> tempList = new ArrayList<>();
        tempList.clear();

        for (POJOGetYearWiseStudentsList obj : pojoGetYearWiseStudentsLists){
            if (obj.getStrName().toUpperCase().contains(query.toUpperCase())){

                tempList.add(obj);
            }
        }

        adapterGetYearWiseStudentsList = new AdapterGetYearWiseStudentsList(tempList,this);
        lvStudentsList.setAdapter(adapterGetYearWiseStudentsList);

    }

    private void getYearWiseStudentsList() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("class",strClass);

        client.post(Urls.getYearWiseStudentsWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getYearWiseStudentsList");

                    if (jsonArray.isNull(0)){
                        lvStudentsList.setVisibility(View.GONE);
                        tvNoStudentsAvailable.setVisibility(View.VISIBLE);
                    }

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strid = jsonObject.getString("id");
                        String strName = jsonObject.getString("name");
                        String strEnrollmentno = jsonObject.getString("enrollmentno");

                        pojoGetYearWiseStudentsLists.add(new POJOGetYearWiseStudentsList(strid,strName,strEnrollmentno));
                    }
                    adapterGetYearWiseStudentsList = new AdapterGetYearWiseStudentsList
                            (pojoGetYearWiseStudentsLists, YearWiseStudentsListActivity.this);

                    lvStudentsList.setAdapter(adapterGetYearWiseStudentsList);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(YearWiseStudentsListActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}