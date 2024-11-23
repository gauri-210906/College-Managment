package com.example.userapplication.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.userapplication.R;

import java.util.List;

public class AdapterAddTaskTeacherGetYear extends BaseAdapter {

    List<POJOAddTaskTeacherGetYear> pojoAddTaskTeacherGetYears;
    Activity activity;

    public AdapterAddTaskTeacherGetYear(List<POJOAddTaskTeacherGetYear> pojoAddTaskTeacherGetYears, Activity activity) {
        this.pojoAddTaskTeacherGetYears = pojoAddTaskTeacherGetYears;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoAddTaskTeacherGetYears.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoAddTaskTeacherGetYears.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater =(LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            holder = new ViewHolder();
            View view1 = inflater.inflate(R.layout.lv_show_all_years_add_task_teacher,null);
            holder.tvyear = view1.findViewById(R.id.tv_show_all_years_add_task_teacher);

            view.setTag(holder);

        }else {

            holder = (ViewHolder) view.getTag();
        }

        final POJOAddTaskTeacherGetYear obj = pojoAddTaskTeacherGetYears.get(position);
        holder.tvyear.setText(obj.getStryear());

        return view;
    }

    class ViewHolder {

        TextView tvyear;

    }

}
