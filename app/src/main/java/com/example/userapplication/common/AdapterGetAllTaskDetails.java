package com.example.userapplication.common;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.userapplication.R;
import com.example.userapplication.student.SubjectWiseTaskActivity;

import java.util.List;

public class AdapterGetAllTaskDetails extends BaseAdapter {

    // baseadapter = load multiple view and pass to AdapterGetAllTaskDetails
    // AdapterGetAllTaskDetails = show multiple design to listview

    List<POJOGetAllTaskDetails> pojoGetAllTaskDetails;
    Activity activity;

    public AdapterGetAllTaskDetails(List<POJOGetAllTaskDetails> list, Activity activity) {
        this.pojoGetAllTaskDetails = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoGetAllTaskDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoGetAllTaskDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_get_all_task,null);

            holder.tvSubject = view.findViewById(R.id.tvgetalltaskTaskSubject);
            holder.tvTaskDescription = view.findViewById(R.id.tvgetalltaskTaskDescription);
            holder.tvTaskDeadline = view.findViewById(R.id.tvgetalltaskTaskDeadline);

            holder.cvTaskList = view.findViewById(R.id.cvlvgetalltaskTaskList);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final POJOGetAllTaskDetails obj = pojoGetAllTaskDetails.get(position);
        holder.tvSubject.setText(obj.getSubject());
        holder.tvTaskDescription.setText(obj.getTaskDescription());
        holder.tvTaskDeadline.setText(obj.getDeadline());


        holder.cvTaskList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SubjectWiseTaskActivity.class);
                i.putExtra("subject",obj.getSubject());
                activity.startActivity(i);
            }
        });

        return view;
    }

    class ViewHolder{

        CardView cvTaskList;
        ImageView ivTaskImage;
        TextView tvSubject, tvTaskDescription, tvTaskDeadline;

    }


}
