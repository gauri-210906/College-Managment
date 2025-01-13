package com.example.userapplication.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterAddNotificationGetYear;
import com.example.userapplication.common.POJOAddNotificationGetYear;
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


public class AdminNotificationFragment extends Fragment {

    ListView lvShowAllYear;
    List<POJOAddNotificationGetYear> pojoAddNotificationGetYears;
    AdapterAddNotificationGetYear adapterAddNotificationGetYear;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_notification, container, false);

        lvShowAllYear = view.findViewById(R.id.lvNotificationFragmentListView);

        pojoAddNotificationGetYears = new ArrayList<>();

        getAllYearList();

        return view;

    }

    private void getAllYearList() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.addTaskTeacherYearWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getAddTaskTeacherYearList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String stryear = jsonObject.getString("year");

                        pojoAddNotificationGetYears.add(new POJOAddNotificationGetYear(id, stryear));

                    }

                    adapterAddNotificationGetYear = new AdapterAddNotificationGetYear(pojoAddNotificationGetYears, getActivity());
                    lvShowAllYear.setAdapter(adapterAddNotificationGetYear);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
