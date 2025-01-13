package com.example.userapplication.admin;

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

import com.example.userapplication.HOD.HODLoginActivity;
import com.example.userapplication.HOD.HodHomeActivity;
import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.example.userapplication.teacher.TeacherMyProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHomeActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(AdminHomeActivity.this);
        editor = preferences.edit();


        bottomNavigationView = findViewById(R.id.AdminHomeBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuAdminHome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu_admin, menu);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId() == R.id.homeMenuAdminLogOut) {

            logout();
         }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(AdminHomeActivity.this);
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
                Intent i = new Intent(AdminHomeActivity.this, HODLoginActivity.class);
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

    AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
    AdminNotificationFragment adminNotificationFragment = new AdminNotificationFragment();
    AdminTaskFragment adminTaskFragment = new AdminTaskFragment();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeBottomNavigationMenuAdminHome){

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAdmin,adminHomeFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuAdminTask) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAdmin,adminTaskFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuAdminNotification) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAdmin,adminNotificationFragment).commit();

        }
        return true;
    }

}