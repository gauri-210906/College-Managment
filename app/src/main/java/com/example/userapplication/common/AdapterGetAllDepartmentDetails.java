package com.example.userapplication.common;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userapplication.teacher.DepartmentWiseYearActivity;
import com.example.userapplication.R;

import java.util.List;

public class AdapterGetAllDepartmentDetails extends RecyclerView.Adapter<AdapterGetAllDepartmentDetails.ViewHolder> {

    List<POJOGetAllDepartmentDetails> pojoGetAllDepartmentDetails;
    Activity activity;

    public AdapterGetAllDepartmentDetails(List<POJOGetAllDepartmentDetails> pojoGetAllDepartmentDetails, Activity activity) {
        this.pojoGetAllDepartmentDetails = pojoGetAllDepartmentDetails;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.rv_get_all_department,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        POJOGetAllDepartmentDetails obj = pojoGetAllDepartmentDetails.get(position);
        viewHolder.tvDepartment.setText(obj.getStrDepartment());

        Glide.with(activity)
                .load(Urls.webServiceAddress+"images/"+obj.getStrDeptimage())
                .skipMemoryCache(false)
                .error(R.drawable.image_not_found)
                .into(viewHolder.ivDeptimage);

        viewHolder.cvDepartmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DepartmentWiseYearActivity.class);
                i.putExtra("department",obj.getStrDepartment());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pojoGetAllDepartmentDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDeptimage;
        TextView tvDepartment;
        CardView cvDepartmentList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDeptimage = itemView.findViewById(R.id.iv_rv_department_image);
            tvDepartment = itemView.findViewById(R.id.tv_rv_department_name);
            cvDepartmentList = itemView.findViewById(R.id.cv_rv_get_all_department_department);

        }
    }
}
