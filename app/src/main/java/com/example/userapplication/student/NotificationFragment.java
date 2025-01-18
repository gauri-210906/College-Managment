package com.example.userapplication.student;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.userapplication.R;
import com.example.userapplication.common.AdapterGetAllNotification;
import com.example.userapplication.common.POJOGetAllNotification;
import com.example.userapplication.common.Urls;
import com.example.userapplication.teacher.AddNotificationActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class NotificationFragment extends Fragment {

    List<POJOGetAllNotification> pojoGetAllNotifications;
    AdapterGetAllNotification adapterGetAllNotification;
    ListView lvNotesList;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        lvNotesList = view.findViewById(R.id.lvNotificationFragmentListView);

        pojoGetAllNotifications = new ArrayList<>();

        getAllNotification();




        return view;
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

                    adapterGetAllNotification = new AdapterGetAllNotification(pojoGetAllNotifications, getActivity());

                    lvNotesList.setAdapter(adapterGetAllNotification);


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