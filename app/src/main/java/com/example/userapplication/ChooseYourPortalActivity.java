package com.example.userapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChooseYourPortalActivity extends AppCompatActivity {

    Button btnExpandTeacher, btnExpandStudent;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_portal);

        btnExpandStudent = findViewById(R.id.btnChooseYourPortalStudents);
        btnExpandTeacher = findViewById(R.id.btnChooseYourPortalTeacher);

        /*btnExpandTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseYourPortalActivity.this, TeacherLogin.class);
                startActivity(i);
            }
        });*/

        btnExpandStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseYourPortalActivity.this, StudentLoginActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}