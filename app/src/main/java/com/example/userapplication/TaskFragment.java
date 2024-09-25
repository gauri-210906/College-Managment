package com.example.userapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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


public class TaskFragment extends Fragment {

    SearchView searchTask;
    ListView lvShowAllTask;
    TextView tvNoTaskAvailable;

    List<POJOGetAllTaskDetails> pojoGetAllTaskDetails;
    AdapterGetAllTaskDetails adapterGetAllTaskDetails;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_task, container, false);

        lvShowAllTask = view.findViewById(R.id.lvTaskFragmentShowAllTask);
        tvNoTaskAvailable = view.findViewById(R.id.tvTaskFragmentNoTaskAvailable);
        pojoGetAllTaskDetails = new ArrayList<>();

        getAllTask();


        searchTask = view.findViewById(R.id.svFragmentTaskSearchView);
        searchTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTask(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                searchTask(query);
                return false;
            }
        });


        return view;
    }

    private void getAllTask() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllTaskDetailsWebService,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getAllTask");
                    if (jsonArray.isNull(0)){
                        tvNoTaskAvailable.setVisibility(View.VISIBLE);
                    }
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strid = jsonObject.getString("id");
                        String strImage = jsonObject.getString("taskimage");
                        String strSubject = jsonObject.getString("subject");
                        String strTaskDescription = jsonObject.getString("taskdescription");
                        String strTaskDeadline = jsonObject.getString("taskdeadline");

                        pojoGetAllTaskDetails.add(new POJOGetAllTaskDetails(strid,strImage,strSubject,strTaskDescription,strTaskDeadline));
                    }

                    adapterGetAllTaskDetails = new AdapterGetAllTaskDetails(pojoGetAllTaskDetails,getActivity());

                    lvShowAllTask.setAdapter(adapterGetAllTaskDetails);



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

    private void searchTask(String query) {

        List<POJOGetAllTaskDetails> tempTask = new ArrayList<>();
        tempTask.clear();

        for (POJOGetAllTaskDetails obj : pojoGetAllTaskDetails){

            if (obj.getSubject().toUpperCase().contains(query.toUpperCase())){

                tempTask.add(obj);

            } else {
                tvNoTaskAvailable.setVisibility(View.VISIBLE);
            }

            adapterGetAllTaskDetails = new AdapterGetAllTaskDetails(tempTask, getActivity());
            lvShowAllTask.setAdapter(adapterGetAllTaskDetails);


        }


    }
}