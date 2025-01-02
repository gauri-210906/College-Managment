package com.example.userapplication.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.HOD.HODLoginActivity;
import com.example.userapplication.R;

public class ChooseTeacherroleActivity extends AppCompatActivity {

    AppCompatButton acbtnTeacher, acbtnHOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_teacherrole);

        acbtnHOD = findViewById(R.id.bt_sign_in_HOD);
        acbtnTeacher = findViewById(R.id.bt_sign_in_teacher);

        acbtnHOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(ChooseTeacherroleActivity.this, HODLoginActivity.class);
                startActivity(intent);

            }
        });

        acbtnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(ChooseTeacherroleActivity.this,TeacherLoginActivity .class);
                startActivity(intent);

            }
        });



    }
}