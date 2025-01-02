package com.example.userapplication.HOD;

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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.teacher.TeacherHomeActivity;
import com.example.userapplication.teacher.TeacherLoginActivity;
import com.example.userapplication.teacher.TeacherMyProfileActivity;
import com.example.userapplication.teacher.teacherAddFragment;
import com.example.userapplication.teacher.teacherHomeFragment;
import com.example.userapplication.teacher.teacherNotificationFragment;
import com.example.userapplication.teacher.teacherTaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HodHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hod_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(HodHomeActivity.this);
        editor = preferences.edit();

        bottomNavigationView = findViewById(R.id.HODHomeBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuTeacherHome);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu_teacher, menu);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeMenuTeacherMyProfile){
            Intent i = new Intent(HodHomeActivity.this, TeacherMyProfileActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.homeMenuTeacherLogOut) {

            logout();
        }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HodHomeActivity.this);
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
                Intent i = new Intent(HodHomeActivity.this, HODLoginActivity.class);
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

    com.example.userapplication.teacher.teacherHomeFragment teacherHomeFragment = new teacherHomeFragment();
    com.example.userapplication.teacher.teacherTaskFragment teacherTaskFragment = new teacherTaskFragment();
    com.example.userapplication.teacher.teacherNotificationFragment teacherNotificationFragment = new teacherNotificationFragment();
    com.example.userapplication.teacher.teacherAddFragment teacherAddFragment = new teacherAddFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherHome){

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHOD,teacherHomeFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherTask) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHOD,teacherTaskFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherNotification) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHOD,teacherNotificationFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTeacherAdd) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHOD,teacherAddFragment).commit();

        }
        return true;
    }
}