package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterDepartmentWiseYear;
import com.example.userapplication.common.POJODepartmentWiseYearList;
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

public class DepartmentWiseYearActivity extends AppCompatActivity {

    ListView lvYearList;
    TextView tvNoDataAvailable;
    String strDepartment;
    List<POJODepartmentWiseYearList> pojoDepartmentWiseList;
    AdapterDepartmentWiseYear adapterDepartmentWiseYear;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_wise_year);

        lvYearList = findViewById(R.id.lvDepartmentWiseYearList);
        tvNoDataAvailable = findViewById(R.id.tvDepartmentWiseYearNoDataAvailable);

        strDepartment = getIntent().getStringExtra("department");

        pojoDepartmentWiseList = new ArrayList<>();


        getDepartmentWiseYear();


    }

    private void getDepartmentWiseYear() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("department",strDepartment);

        client.post(Urls.getAllDepartmentWiseYearList,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getDepartmentWiseYear");
                    if (jsonArray.isNull(0)){
                        tvNoDataAvailable.setVisibility(View.VISIBLE);
                        lvYearList.setVisibility(View.GONE);
                    }
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strID = jsonObject.getString("id");
                        String strDepartmentName = jsonObject.getString("department");
                        String strYear = jsonObject.getString("class");

                        pojoDepartmentWiseList.add(new POJODepartmentWiseYearList(strID,strDepartmentName,strYear));
                    }

                    adapterDepartmentWiseYear = new AdapterDepartmentWiseYear(pojoDepartmentWiseList,DepartmentWiseYearActivity.this);

                    lvYearList.setAdapter(adapterDepartmentWiseYear);



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(DepartmentWiseYearActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }

        });



    }
}