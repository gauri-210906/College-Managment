package com.example.userapplication.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.userapplication.R;
import com.example.userapplication.teacher.ShowStudentWiseDetailsActivity;

import java.util.List;

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

        return view;
    }


    class ViewHolder{
        TextView tvStudentName, tvEnrollmentno, ivStudentDelete;
    }

}
