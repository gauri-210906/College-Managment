package com.example.userapplication.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapplication.R;
import com.example.userapplication.teacher.ShowStudentWiseDetailsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdapterGetYearWiseStudentsList extends BaseAdapter {

    List<POJOGetYearWiseStudentsList> list;
    Activity activity;

    public AdapterGetYearWiseStudentsList(List<POJOGetYearWiseStudentsList> pojoGetYearWiseStudentsLists, Activity activity) {
        this.list = pojoGetYearWiseStudentsLists;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "WrongViewCast"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_year_wise_students_list,null);

            holder.tvStudentName = view.findViewById(R.id.tv_year_wise_students_list_student_name);
            holder.tvEnrollmentno = view.findViewById(R.id.tv_year_wise_students_list_enrollment_number);
            holder.ivStudentDelete = view.findViewById(R.id.iv_year_wise_students_list_delete);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final POJOGetYearWiseStudentsList obj = list.get(position);
        holder.tvStudentName.setText(obj.getStrName());
        holder.tvEnrollmentno.setText(obj.getStrEnrollmentno());

        holder.tvStudentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ShowStudentWiseDetailsActivity.class);
                i.putExtra("enrollmentno",obj.getStrEnrollmentno());
                activity.startActivity(i);
            }
        });

        holder.ivStudentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                ad.setTitle("Are you sure you want to delete student ?");
                ad.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                ad.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteStudent(obj.getStrEnrollmentno(),position);

                    }
                }).create().show();
            }
        });

        return view;
    }

    private void deleteStudent(String strEnrollmentno, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("enrollmentno",strEnrollmentno);
        client.post(Urls.deleteStudentWebService,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        list.remove(position);
                        notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    class ViewHolder{
        TextView tvStudentName, tvEnrollmentno;
        ImageView ivStudentDelete;
    }

}
