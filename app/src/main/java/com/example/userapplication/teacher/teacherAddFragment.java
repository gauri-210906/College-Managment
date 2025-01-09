package com.example.userapplication.teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.userapplication.AskAiActivity;
import com.example.userapplication.R;


public class teacherAddFragment extends Fragment {

    ImageView ivAI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_add, container, false);

        ivAI = view.findViewById(R.id.ivTeacherAddFragmentAI);

        ivAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AskAiActivity.class);
                startActivity(i);

            }
        });


        return view;
    }
}