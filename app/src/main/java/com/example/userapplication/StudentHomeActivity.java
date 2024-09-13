package com.example.userapplication;

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

import com.example.userapplication.common.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(StudentHomeActivity.this);
        editor = preferences.edit();

        bottomNavigationView = findViewById(R.id.studentHomeBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuHome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeMenuMyProfile){
            Intent i = new Intent(StudentHomeActivity.this, StudentMyProfileActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.homeMenuLogOut) {

            logout();
        }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(StudentHomeActivity.this);
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
                Intent i = new Intent(StudentHomeActivity.this, StudentLoginActivity.class);
                startActivity(i);
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

    HomeFragment homeFragment = new HomeFragment();
    TaskFragment taskFragment = new TaskFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    AddFragment addFragment = new AddFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeBottomNavigationMenuHome){

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,homeFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTask) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,taskFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuNotification) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,notificationFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuAdd) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,addFragment).commit();

        }


        return true;
    }
}