package com.example.userapplication.common;

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

import androidx.cardview.widget.CardView;

import com.example.userapplication.R;
import com.example.userapplication.teacher.AddNotificationActivity;
import com.example.userapplication.teacher.showYearWiseNotificationDetailsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdapterGetAllNotification extends BaseAdapter {

    List<POJOGetAllNotification> pojoGetAllNotifications;
    Activity activity;

    public AdapterGetAllNotification(List<POJOGetAllNotification> pojoGetAllNotifications, Activity activity) {
        this.pojoGetAllNotifications = pojoGetAllNotifications;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoGetAllNotifications.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoGetAllNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater =(LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_get_all_notification,null);
            holder.tvTitle = view.findViewById(R.id.lv_get_all_notification_title);
            holder.tvYear = view.findViewById(R.id.lv_get_all_notification_year);
            holder.cvNote = view.findViewById(R.id.cv_get_all_notification);
            holder.ivDelete = view.findViewById(R.id.iv_get_all_notification_Delete_Note);


            view.setTag(holder);

        }else {

            holder = (ViewHolder) view.getTag();
        }

        final POJOGetAllNotification obj = pojoGetAllNotifications.get(position);
        holder.tvYear.setText(obj.getYear());
        holder.tvTitle.setText(obj.getTitle());

        holder.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, showYearWiseNotificationDetailsActivity.class);
                i.putExtra("id",obj.getId());
                activity.startActivity(i);

            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
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

                        deleteNote(obj.getId(),position);

                    }
                }).create().show();
            }
        });




        return view;
    }

    private void deleteNote(String id, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("id",id);
        client.post(Urls.deleteNotificationTeacherWebService,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        pojoGetAllNotifications.remove(position);
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


    class ViewHolder {

        TextView tvTitle,tvYear;
        CardView cvNote;
        ImageView ivDelete, ivEdit;

    }


}
