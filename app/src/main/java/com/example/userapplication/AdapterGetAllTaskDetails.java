package com.example.userapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.userapplication.common.Urls;

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
            holder.ivTaskImage = view.findViewById(R.id.lvivgetalltaskTaskImage);
            holder.tvSubject = view.findViewById(R.id.tvgetalltaskTaskSubject);
            holder.tvTaskDescription = view.findViewById(R.id.tvgetalltaskTaskDescription);
            holder.tvTaskDeadline = view.findViewById(R.id.tvgetalltaskTaskDeadline);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final POJOGetAllTaskDetails obj = pojoGetAllTaskDetails.get(position);
        holder.tvSubject.setText(obj.getSubject());
        holder.tvTaskDescription.setText(obj.getTaskDescription());
        holder.tvTaskDeadline.setText(obj.getDeadline());

        Glide.with(activity)
                .load(Urls.webServiceAddress+"images/"+obj.getTaskImage())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.image_not_found)
                .into(holder.ivTaskImage);


        return view;
    }

    class ViewHolder{
        ImageView ivTaskImage;
        TextView tvSubject, tvTaskDescription, tvTaskDeadline;

    }


}
