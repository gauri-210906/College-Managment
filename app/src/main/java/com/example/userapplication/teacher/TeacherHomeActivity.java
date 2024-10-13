package com.example.userapplication.teacher;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(TeacherHomeActivity.this);
        editor = preferences.edit();

        bottomNavigationView = findViewById(R.id.teacherHomeBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuTeacherHome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu_teacher, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeMenuTeacherMyProfile){
            Intent i = new Intent(TeacherHomeActivity.this, TeacherMyProfileActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.homeMenuTeacherLogOut) {

            logout();
        }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(TeacherHomeActivity.this);
        /* is used to create alert dialog box */
        ad.setTitle("I.E.T.K.");
        ad.setMessage("Are you sure you want to log out ?");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(TeacherHomeActivity.this, TeacherLoginActivity.class);
                editor.putBoolean("isLogin",false).commit();
                startActivity(i);
                finish();
            }
        }).create().show();


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

    teacherHomeFragment teacherHomeFragment = new teacherHomeFragment();
    teacherTaskFragment teacherTaskFragment = new teacherTaskFragment();
    teacherNotificationFragment teacherNotificationFragment = new teacherNotificationFragment();
    teacherAddFragment teacherAddFragment = new teacherAddFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherHome){

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutTeacher,teacherHomeFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherTask) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutTeacher,teacherTaskFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherNotification) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutTeacher,teacherNotificationFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherAdd) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutTeacher,teacherAddFragment).commit();

        }
        return true;
    }
}