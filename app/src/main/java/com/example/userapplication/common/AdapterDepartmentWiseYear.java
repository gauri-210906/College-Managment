package com.example.userapplication.common;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.userapplication.R;
import com.example.userapplication.teacher.YearWiseStudentsListActivity;

import java.util.List;

public class AdapterDepartmentWiseYear extends BaseAdapter {

    List<POJODepartmentWiseYearList> list;
    Activity activity;

    public AdapterDepartmentWiseYear(List<POJODepartmentWiseYearList> list, Activity activity) {
        this.list = list;
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_department_wise_year_list,null);
            holder.tvDepartmentWiseYear = view.findViewById(R.id.tv_department_wise_year_list_year);
            holder.cvYearWiseStudentsList = view.findViewById(R.id.cv_department_wise_year_list);


            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final POJODepartmentWiseYearList obj = list.get(position);
        holder.tvDepartmentWiseYear.setText(obj.getStrYear());


        holder.cvYearWiseStudentsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, YearWiseStudentsListActivity.class);
                i.putExtra("class",obj.getStrYear());
                activity.startActivity(i);

            }
        });


        return view;
    }

    class ViewHolder{

        TextView tvDepartmentWiseYear;
        CardView cvYearWiseStudentsList;

    }


}
