package com.example.userapplication.teacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterAddTaskTeacherGetYear;
import com.example.userapplication.common.POJOAddTaskTeacherGetYear;
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


public class teacherTaskFragment extends Fragment {

    ListView lvShowAllYear;
    List<POJOAddTaskTeacherGetYear> pojoAddTaskTeacherGetYears;
    AdapterAddTaskTeacherGetYear adapterAddTaskTeacherGetYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_teacher_task, container, false);
        lvShowAllYear = view.findViewById(R.id.lvTeacherTaskFragmentTaskListView);

        pojoAddTaskTeacherGetYears = new ArrayList<>();

        getAllYearList();   // get add task year list teacher fragment

        return view;
    }

    private void getAllYearList() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.addTaskTeacherYearWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getAddTaskTeacherYearList");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String stryear = jsonObject.getString("year");

                        pojoAddTaskTeacherGetYears.add(new POJOAddTaskTeacherGetYear(id,stryear));

                    }

                    adapterAddTaskTeacherGetYear = new AdapterAddTaskTeacherGetYear(pojoAddTaskTeacherGetYears,getActivity());
                    lvShowAllYear.setAdapter(adapterAddTaskTeacherGetYear);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                /*Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();*/
            }
        });
    }
}