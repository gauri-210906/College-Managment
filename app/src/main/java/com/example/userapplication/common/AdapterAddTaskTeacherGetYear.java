package com.example.userapplication.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.userapplication.R;
import com.example.userapplication.teacher.AddYearWiseStudentTaskActivity;
import com.example.userapplication.teacher.YearWiseStudentsListActivity;

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
            view = inflater.inflate(R.layout.lv_show_all_years_add_task_teacher,null);
            holder.tvyear = view.findViewById(R.id.tv_show_all_years_add_task_teacher);
            holder.cvyear = view.findViewById(R.id.cv_show_all_years_add_task_teacher);

            view.setTag(holder);

        }else {

            holder = (ViewHolder) view.getTag();
        }

        final POJOAddTaskTeacherGetYear obj = pojoAddTaskTeacherGetYears.get(position);
        holder.tvyear.setText(obj.getStryear());

        holder.cvyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, AddYearWiseStudentTaskActivity.class);
                i.putExtra("year",obj.getStryear());
                activity.startActivity(i);

            }
        });
        return view;
    }

    class ViewHolder {

        TextView tvyear;
        CardView cvyear;

    }

}
