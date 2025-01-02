package com.example.userapplication.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.userapplication.R;
import com.example.userapplication.SplashActivity;
import com.google.zxing.client.android.Intents;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    ImageSlider imageSlider;
    ArrayList<SlideModel> slideModelArrayList = new ArrayList<>();
    CardView cvQRCode, cvQRScanner,cvTask, cvNotification, cvFeedback, cvSchedule;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = view.findViewById(R.id.isStudentHomeFragmentImageSlider);

        slideModelArrayList.add(new SlideModel(R.drawable.co, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.ce, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.me, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.ej, ScaleTypes.FIT));


        imageSlider.setImageList(slideModelArrayList,ScaleTypes.FIT);
        imageSlider.setSlideAnimation(AnimationTypes.FIDGET_SPINNER);
        imageSlider.startSliding(1000);// with new period

        cvQRCode = view.findViewById(R.id.cv_student_home_QR_Code);
        cvQRScanner = view.findViewById(R.id.cv_student_home_scanner);
        cvFeedback = view.findViewById(R.id.cv_student_home_feedback);
        cvTask = view.findViewById(R.id.cv_student_home_task);
        cvNotification = view.findViewById(R.id.cv_student_home_notification);

        cvQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QRCodeActivity.class);
                startActivity(i);

            }
        });
        cvQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanQRCodeActivity.class);
                startActivity(i);

            }
        });
        cvTask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onClick(View v) {
               // Bundle bundle = new Bundle();
                //TaskFragment taskFragment = new TaskFragment();
               // bundle.putString("key",textbox.getText().toString());
               // taskFragment.setArguments(bundle);
                Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.scroll_view,new TaskFragment()).commit();
            }
        });
        cvNotification.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.scroll_view,new TaskFragment()).commit();
            }
        });
        cvFeedback.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.scroll_view,new TaskFragment()).commit();
            }
        });

        return view;
    }
}