package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userapplication.R;
import com.example.userapplication.common.AdapterGetAllDepartmentDetails;
import com.example.userapplication.common.POJOGetAllDepartmentDetails;
import com.example.userapplication.common.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class teacherHomeFragment extends Fragment {

    RecyclerView rvGetAllDepartment;
    CardView cvAllStudentLocation;
    List<POJOGetAllDepartmentDetails> pojoGetAllDepartmentDetails;
    AdapterGetAllDepartmentDetails adapterGetAllDepartmentDetails;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_teacher_home, container, false);

       rvGetAllDepartment = view.findViewById(R.id.rvTeacherHomeFragmentDepartmentList);
       cvAllStudentLocation = view.findViewById(R.id.cvTeacherHomeFragmentStudentLocation);


       cvAllStudentLocation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getActivity(), ViewAllStudentsLocationActivity.class);
               startActivity(i);
           }
       });


       rvGetAllDepartment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

       pojoGetAllDepartmentDetails = new ArrayList<>();
       adapterGetAllDepartmentDetails = new AdapterGetAllDepartmentDetails(pojoGetAllDepartmentDetails,getActivity());
       rvGetAllDepartment.setAdapter(adapterGetAllDepartmentDetails);

       getAllDepartmentlist();

       return view;
    }

    private void getAllDepartmentlist() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.getAllDepartmentTeacherWebService,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("getAllDepartment");

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String strID = jsonObject1.getString("id");
                                String strDepartment = jsonObject1.getString("department");
                                String strDeptImage = jsonObject1.getString("deptimage");

                                pojoGetAllDepartmentDetails.add(new POJOGetAllDepartmentDetails(strID,strDepartment,strDeptImage));

                            }

                            adapterGetAllDepartmentDetails.notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(stringRequest);


    }
}